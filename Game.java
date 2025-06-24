import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import tiles.Tile;
import plants.Plant;
import plants.Sunflower;
import plants.Peashooter;
import plants.Cherrybomb;
import zombies.Zombie;
import zombies.NormalZombie;
import zombies.FlagZombie;
import zombies.ConeheadZombie;

/**
 * The {@code Game} class encapsulates the core logic for the 
 * console version of the Plants vs Zombies simulation. 
 * 
 * It handles board initialization, level setup, sun generation,
 * zombie spawning, plant placement, attacks, zombie movement,
 * and win/loss conditions. This class separates game logic
 * from the main driver and UI rendering.
 * 
 * Usage:
 * {@code new Game().start();}
 */
public class Game {

    /** Number of rows on the board. */
    private static final int ROWS = 5;

    /** Number of columns on the board. */
    private static final int COLS = 9;

    /** Column index representing the player's house. */
    private static final int HOUSE_COLUMN = 0;

    /** First column index where plants may be placed. */
    private static final int FIRST_PLANTABLE_COLUMN = HOUSE_COLUMN + 1;

    /** Total duration of the game in ticks. */
    private static final int GAME_DURATION = 180;

    /** 2D grid representing the game board. */
    private Tile[][] board;

    /** Utility for rendering the board state. */
    private GameBoard gameBoard;

    /** Scanner for player input. */
    private Scanner scanner;

    /** Random generator for zombie spawning. */
    private Random rand;

    /** Player-selected difficulty level (1-3). */
    private int level;

    /** The wave of zombies for a given level. */
    private int waveLimit;

    /** Current available sun points. */
    private int sun;

    /** Current time tick. */
    private int ticks;

    /**
     * Tracks zombies spawned in the current tick to prevent immediate movement.
     */
    private List<Zombie> justSpawned = new ArrayList<>();

    /**
     * Constructs a new {@code Game} object. The board itself is not created at
     * this stage; it will be initialized once {@link #start()} is invoked. The
     * constructor simply prepares the input scanner and pseudo-random number
     * generator used for zombie spawning and user interaction.
     */
    public Game() {
        scanner = new Scanner(System.in);
        rand = new Random();
    }

    /**
     * Initializes the game board and attaches the rendering utility.
     *
     * Index:       0       1  2  3  4  5  6  7                     8
     * Meaning: [house]           LAWN             [zombie spawn at last column]
     */
    private void initializeBoard() {
        board = new Tile[ROWS][COLS];
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                board[r][c] = new Tile(r, c);
            }
        }
        gameBoard = new GameBoard(board);
    }

    /**
     * Prompts the player to select a difficulty level from 1-3.
     * The chosen level determines the final zombie wave size.
     */
    private void selectLevel() {
        System.out.print("Select Level (1, 2, or 3): ");
        try {
            level = Integer.parseInt(scanner.nextLine());
        } catch (Exception e) {
            level = 1;
        }
        if (level < 1) level = 1;                       // Select default level 1 if input less than 1
        if (level > 3) level = 3;                       // Select default level 3 if input greater than 3

        if (level == 1) waveLimit = 5;
        if (level == 2) waveLimit = 7;
        if (level == 3) waveLimit = 9;
    }

    /**
     * Generates 25 sun points every 5 ticks if the current
     * tick count is divisible by five.
     */
    private void dropSun() {                            // Drop sun every 5 seconds
        if (ticks % 5 == 0) {
            sun += 25;
            System.out.println("Sun dropped! Current sun: " + sun);
        }
    }

    /**
     * Spawns zombies following the level's timing pattern. Early waves spawn less frequently while later waves increase the rate. Newly created zombies are added to {@code justSpawned} so they remain stationary for one tick.
     */
    private void spawnZombies() {

        // Determine if a zombie should spawn based on the current tick window
        if (ticks >= 30 && ticks <= 80 && ticks % 10 == 0) {                                        // 30 - 80 Spawn every 10
            spawnSingleZombie();
        } else if (ticks >= 81 && ticks <= 140 && ticks % 5 == 0) {                                 // 81 - 140 Spawn every 5
            spawnSingleZombie();
        } else if (ticks >= 141 && ticks <= 170 && ticks % 3 == 0) {                                // 141 - 170 Spawn every 3
            spawnSingleZombie();
        } else if (ticks == 171) {
            for (int waveCount = 0; waveCount< waveLimit;waveCount++) {
                spawnSingleZombie();
            }
        }
    }

    /**
     * Spawns a single zombie on the right-most column. A random row and
     * zombie type are selected. The newly created zombie is added to the
     * {@link #justSpawned} list so that it does not move until the next game
     * tick.
     */
    private void spawnSingleZombie() {
        // Uses Pseudo-random for random spawn
        // Randomly choose a row and zombie type
        int row = rand.nextInt(ROWS);
        // If ROWS = 5, then row can be 0, 1, 2, 3, or 4.
        Tile spawnTile = board[row][COLS - 1];
        // Uses LAST COLUMN (COLS - 1) to spawn
        Zombie z;
        int zType = rand.nextInt(3);
        // Choose random number from 0 to 2
        if (zType == 0) {
            // 0 is Normal Zombie
            z = new NormalZombie(spawnTile);
        } else if (zType == 1) {
            // 1 is Flag Zombie
            z = new FlagZombie(spawnTile);
        } else {
            // 2 is Conehead Zombie
            z = new ConeheadZombie(spawnTile);
        }
        // We can scale this further to add zombies!!!
        spawnTile.addZombie(z);
        // Add zombie to the tile
        justSpawned.add(z);
        // When a zombie just got spawned, we put them in a justSpawned array list.
        System.out.println(
                // those who exist in that array list cannot call their move() methods the same second they got spawned

                // This prevents the scenario where a zombie moves immediately and skips a tile after spawn.
                "Zombie appeared in Row " + (row + 1) + ", Column " + COLS
                        + " | Type: " + z.getClass().getSimpleName()
                        + " | Health=" + z.getHealth() + ", Speed=" + z.getSpeed());
    }
    /**
     * Loops over the board and triggers each Sunflower to produce sun.
     * Sunflowers generate additional resources every 2 ticks.
     */
    private void handleSunflowers() {
        for (int r = 0; r < ROWS; r++) {                                                            // Get Row
            for (int c = 0; c < COLS; c++) {                                                        // Get Column
                Plant p = board[r][c].getPlant();                                                   // Check if Sunflower. THIS LOGIC IS USED OVER AND OVER
                if (p instanceof Sunflower && ticks % 2 == 0) {                                     // Generate sun every 2 seconds
                    Sunflower s = (Sunflower) p;
                    sun = s.action(sun);
                }
            }
        }
    }

    /**
     * Handles player input for placing plants.
     * The player may choose Sunflower (50 sun), Peashooter (100 sun)
     * or Cherrybomb (150 sun). Planting is disallowed on the house column.
     */
    private void placePlant() {
        System.out.print("Place plant? (1-Sunflower, 2-Peashooter, 3-Cherrybomb, 0-None):");
        int choice = -1;
        try {

            /* Read the player's choice; invalid input defaults to -1, if it is an invalid input we assume
               that the player just wants to let the time pass by.                                          */

            choice = Integer.parseInt(scanner.nextLine());
        } catch (Exception e) {
            choice = -1;
        }
            /*
                This section handles the logic of :
                    1. Choosing the type of plant to place
                    2. Boundary checking for correct locations, if incorrect move to the next tick.
                       It does not loop waiting for a correct input
                    3. If you have enough sun to plant
             */
    
        if (choice == 1 && sun >= 50) {
            System.out.print("Enter row (1-5) and column (2-9): ");
            int r = scanner.nextInt() - 1;
            int c = scanner.nextInt() - 1;
            scanner.nextLine();
            // Validate location and ensure tile is empty
            if (r >= 0 && r < ROWS && c >= FIRST_PLANTABLE_COLUMN && c < COLS && !board[r][c].isOccupied()) {
                board[r][c].setPlant(new Sunflower(board[r][c]));
                sun -= 50;
                System.out.println("Placed Sunflower at Row " + (r + 1) + " Column " + (c + 1));
            } else {
                System.out.println("Invalid location. Cannot place on the house (column " + (HOUSE_COLUMN + 1) + ").");
            }
        } else if (choice == 2 && sun >= 100) {
            System.out.print("Enter row (1-5) and column (2-9): ");
            int r = scanner.nextInt() - 1;
            int c = scanner.nextInt() - 1;
            scanner.nextLine();
            // Validate location and ensure tile is empty
            if (r >= 0 && r < ROWS && c >= FIRST_PLANTABLE_COLUMN && c < COLS && !board[r][c].isOccupied()) {
                board[r][c].setPlant(new Peashooter(board[r][c]));
                sun -= 100;
                System.out.println("Placed Peashooter at Row " + (r + 1) + " Column " + (c + 1));
            } else {
                System.out.println("Invalid location. Cannot place on the house (column " + (HOUSE_COLUMN + 1) + ").");
            }
        } else if (choice == 3 && sun >= 150) {
            System.out.print("Enter row (1-5) and column (2-9): ");
            int r = scanner.nextInt() - 1;
            int c = scanner.nextInt() - 1;
            scanner.nextLine();
            // Validate location and ensure tile is empty
            if (r >= 0 && r < ROWS && c >= FIRST_PLANTABLE_COLUMN && c < COLS && !board[r][c].isOccupied()) {
                board[r][c].setPlant(new Cherrybomb(board[r][c]));
                sun -= 150;
                System.out.println("Placed Cherrybomb at Row " + (r + 1) + " Column " + (c + 1));
            } else {
                System.out.println("Invalid location. Cannot place on the house (column " + (HOUSE_COLUMN + 1) + ").");
            }
        } else if (choice == 1 || choice == 2 || choice == 3) {
            System.out.println("Not enough sun to plant that!");
        }
    }

    /**
     * Iterates through all Peashooters and attacks the first zombie within range.
     * Eliminates zombies with zero health after being hit.
     */
    private void handlePeashooters() {

        for (int r = 0; r < ROWS; r++) {                                                        // ROW
            for (int c = 0; c < COLS; c++) {                                                    // COLUMN
                Plant p = board[r][c].getPlant();                                               // Get Plant per Tile
                if (p instanceof Peashooter) {                                                  // Check if Peashooter
                    boolean hasAttacked = false;                                                // Every PeaShooter can only attack 1 zombie
                                                                                                // Look at own column (zc = c) then increment with zc++ to check to the right
                    for (int zc = c; zc < COLS && zc <= c + p.getRange() && !hasAttacked; zc++) {               // The Peashooter’s maximum range to the right (c + p.getRange())
                        if (board[r][zc].hasZombies()) {                                        // But never past the edge of the board zc < COLS
                            List<Zombie> zs = board[r][zc].getZombies();                        // Get list of zombies
                            if (!zs.isEmpty()) {                                                // Check if we are not accessing a null value
                                Zombie target = zs.get(0);                                      // Get the first zombie
                                p.action(target);                                               // Call PeaShooter action method
                                if (!target.isAlive()) {                                        // Update Logic
                                    board[r][zc].removeZombie(target);
                                    System.out.println(
                                            "Zombie at Row " + (r + 1) + " Col " + (zc + 1) + " died.");
                                }
                            }
                            hasAttacked = true; //                                              // Shoot only 1 zombie
                        }
                    }
                }
            }
        }
    }

    /**
     * Updates all active Cherrybombs each tick. When a bomb's fuse expires it explodes
     * damaging nearby zombies before removing itself from the board.
     */
    private void handleCherrybombs() {
            /*
               Same logic as Sunflower handleSunflowers()
             */
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                Plant p = board[r][c].getPlant();
                if (p instanceof Cherrybomb) {
                    Cherrybomb cb = (Cherrybomb) p;
                    // Advance fuse timer and explode when ready
                    cb.tick(board);
                    if (!cb.isAlive()) {
                        board[r][c].removePlants();
                    }
                }
            }
        }
    }

    /**
     * Moves all zombies that were not just spawned this tick and handles their attacks.
     * Checks if any zombie reaches the player's house.
     *
     * @return {@code true} if a zombie reached the house; {@code false} otherwise.
     */
    private boolean moveZombies() {
        List<Zombie> movingZombies = new ArrayList<>();
        // Collect all zombies from every tile first

            /*
               Same logic as Sunflower handleSunflowers()
             */

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                movingZombies.addAll(board[r][c].getZombies());
            }
        }

        for (Zombie z : movingZombies) {
            z.attack();                         // WE CAN CHANGE GAME LOGIC HERE TO ATTACK WITH COOL DOWN, IF NEEDED!
                                                // But damage is too low for zombies.

            Tile current = z.getPosition();
            Plant occupant = current.getPlant();

            /*
                Movement logic requirements:
                    1. Only move if there is no living plant on the current tile.
                    2. Skip movement for newly spawned zombies, (this prevents z.move() being called on the same tick it is spawned)
             */

            if (occupant == null) {
                if (!justSpawned.contains(z) && ticks % z.getSpeed() == 0) {
                    z.move(board);
                }
            }

            // Check if any zombie reached the house column
            if (z.getPosition().getColumn() == 0) {
                System.out.println("A zombie reached your house! Game Over. Zombies win!");
                return true;
            }
        }
        return false;
    }

    /**
     * Formats a tick count as a MM:SS time string.
     *
     * @param ticks current tick count
     * @return formatted time string
     */
    private static String formatTime(int ticks) {
        int minutes = ticks / 60;
        int seconds = ticks % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    /**
     * Starts the main game loop after setting up the board and difficulty.
     * Each tick handles sun generation, zombie spawning, plant actions, movement,
     * and board rendering while checking for win or loss conditions.
     */
    public void start() {
        initializeBoard();
        selectLevel();

        sun = 150;
        ticks = 0;

        System.out.println("=== Plants vs Zombies Console Game ===");
        System.out.println("Level: " + level);

        gameBoard.display();

        // Main game loop: each iteration represents one tick
        while (ticks <= GAME_DURATION) {
            System.out.println("Time: " + formatTime(ticks));

            justSpawned.clear(); // clear tracker for this tick

            // === Begin per-tick phases ===
            dropSun();
            spawnZombies();
            handleSunflowers();
            System.out.println("Current Sun: " + sun);
            placePlant();
            handlePeashooters();
            handleCherrybombs();
            if (moveZombies()) {
                scanner.close();
                return;
            }
            // === End per-tick phases ===

            gameBoard.display();

            if (ticks == GAME_DURATION) {
                System.out.println("Time's up! Plants survived. Plants win!");
                scanner.close();
                return;
            }

            ticks++;
        }
    }
}

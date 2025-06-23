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

    /** Current available sun points. */
    private int sun;

    /** Current time tick. */
    private int ticks;

    /**
     * Tracks zombies spawned in the current tick to prevent immediate movement.
     */
    private List<Zombie> justSpawned = new ArrayList<>();

    /** Constructs a new Game instance with initialized input and random generator. */
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
     * Prompts the player to select a level and validates input.
     */
    private void selectLevel() {
        System.out.print("Select Level (1, 2, or 3): ");
        try {
            level = Integer.parseInt(scanner.nextLine());
        } catch (Exception e) {
            level = 1;
        }
        if (level < 1) level = 1;
        if (level > 3) level = 3;
    }

    /**
     * Generates ambient sun points every 5 ticks.
     */
    private void dropSun() {
        if (ticks % 5 == 0) {
            sun += 25;
            System.out.println("Sun dropped! Current sun: " + sun);
        }
    }

    /**
     * Spawns zombies according to the wave pattern defined in the project specs.
     * Newly spawned zombies are tracked to prevent instant movement.
     */
    private void spawnZombies() {
        boolean spawnZombie = false;
        if (ticks >= 30 && ticks <= 80 && ticks % 10 == 0) {
            spawnZombie = true;
        } else if (ticks >= 81 && ticks <= 140 && ticks % 5 == 0) {
            spawnZombie = true;
        } else if (ticks >= 141 && ticks <= 170 && ticks % 3 == 0) {
            spawnZombie = true;
        } else if (ticks >= 171 && ticks <= 180) {
            spawnZombie = true;
        }

        if (spawnZombie) {
            int row = rand.nextInt(ROWS);
            Tile spawnTile = board[row][COLS - 1];
            Zombie z;
            int zType = rand.nextInt(3);
            if (zType == 0) {
                z = new NormalZombie(spawnTile);
            } else if (zType == 1) {
                z = new FlagZombie(spawnTile);
            } else {
                z = new ConeheadZombie(spawnTile);
            }
            spawnTile.addZombie(z);
            justSpawned.add(z);
            System.out.println(
                    "Zombie appeared in Row " + (row + 1) + ", Column " + COLS
                            + " | Type: " + z.getClass().getSimpleName()
                            + " | Health=" + z.getHealth() + ", Speed=" + z.getSpeed());
        }
    }

    /**
     * Generates sun points from all active Sunflowers every 2 ticks.
     */
    private void handleSunflowers() {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                Plant p = board[r][c].getPlant();
                if (p instanceof Sunflower && ticks % 2 == 0) {
                    Sunflower s = (Sunflower) p;
                    sun = s.action(sun);
                }
            }
        }
    }

    /**
 * Handles player input for placing Sunflowers and Peashooters.
 * Prevents planting on the house tile (column 0).
 * Allows planting anywhere else, including the zombie edge if desired.
 */
    private void placePlant() {
        System.out.print("Place plant? (1-Sunflower, 2-Peashooter, 3-Cherrybomb, 0-None):");
        int choice = -1;
        try {
            choice = Integer.parseInt(scanner.nextLine());
        } catch (Exception e) {
            choice = -1;
        }
    
        if (choice == 1 && sun >= 50) {
            System.out.print("Enter row (1-5) and column (2-9): ");
            int r = scanner.nextInt() - 1;
            int c = scanner.nextInt() - 1;
            scanner.nextLine();
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
            if (r >= 0 && r < ROWS && c >= FIRST_PLANTABLE_COLUMN && c < COLS && !board[r][c].isOccupied()) {
                board[r][c].setPlant(new Cherrybomb(board[r][c]));
                sun -= 150;
                System.out.println("Placed Cherrybomb at Row " + (r + 1) + " Column " + (c + 1));
            } else {
                System.out.println("Invalid location. Cannot place on the house (column " + (HOUSE_COLUMN + 1) + ").");
            }
        }
    }

    /**
     * Handles Peashooter attacks: finds the nearest zombie in range and applies damage.
     * Removes zombies that die as a result.
     */
    private void handlePeashooters() {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                Plant p = board[r][c].getPlant();
                if (p instanceof Peashooter) {
                    for (int zc = c + 1; zc < COLS && zc <= c + p.getRange(); zc++) {
                        if (board[r][zc].hasZombies()) {
                            List<Zombie> zs = board[r][zc].getZombies();
                            if (!zs.isEmpty()) {
                                Zombie target = zs.get(0);
                                p.action(target);
                                if (!target.isAlive()) {
                                    board[r][zc].removeZombie(target);
                                    System.out.println(
                                            "Zombie at Row " + (r + 1) + " Col " + (zc + 1) + " died.");
                                }
                            }
                            break; // attack only one zombie on the first tile in range
                        }
                    }
                }
            }
        }
    }

    /**
     * Updates all Cherrybombs each tick and triggers explosions when ready.
     */
    private void handleCherrybombs() {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                Plant p = board[r][c].getPlant();
                if (p instanceof Cherrybomb) {
                    Cherrybomb cb = (Cherrybomb) p;
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
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                movingZombies.addAll(board[r][c].getZombies());
            }
        }

        for (Zombie z : movingZombies) {
            z.attack();

            // Only move if there is no living plant on the current tile
            Tile current = z.getPosition();
            Plant occupant = current.getPlant();

            if (occupant == null) {
                if (!justSpawned.contains(z) && ticks % z.getSpeed() == 0) {
                    z.move(board);
                }
            }

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
     * Starts the game loop, controlling all phases: sun generation,
     * zombie spawning, plant actions, zombie movement, rendering,
     * and win/loss checking.
     */
    public void start() {
        initializeBoard();
        selectLevel();

        sun = 150;
        ticks = 0;

        System.out.println("=== Plants vs Zombies Console Game ===");
        System.out.println("Level: " + level);

        gameBoard.display();

        while (ticks <= GAME_DURATION) {
            System.out.println("Time: " + formatTime(ticks));

            justSpawned.clear(); // clear tracker for this tick

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

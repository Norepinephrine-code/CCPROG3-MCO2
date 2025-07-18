import events.GameEventListener;
import gui.GameBoardGUI;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import javax.swing.Timer;
import plants.Cherrybomb;
import plants.FreezePeashooter;
import plants.Peashooter;
import plants.Plant;
import plants.PotatoMine;
import plants.Sunflower;
import plants.Wallnut;
import tiles.Tile;
import zombies.BucketHeadZombie;
import zombies.ConeheadZombie;
import zombies.FlagZombie;
import zombies.NormalZombie;
import zombies.PoleVaultingZombie;
import zombies.Zombie;

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
public class Game implements GameEventListener {

    /** Number of rows on the board. */
    private static int ROWS;

    /** Number of columns on the board. */
    private static int COLS;

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

    /** Optional Swing GUI representation of the board. */
    private GameBoardGUI gameBoardGUI;

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

    /** Zombie Win Flag */
    private boolean ZombiesWin = false;

    /** Plant Win Flag */
    private boolean PlantsWin = false;

    /** Timer */
    private Timer timer;

    /** Selected Plant Type */
    private int selectedPlant = 0;

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
    public Game(int level) {
        this.level = level;
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

        System.out.println("Initializing Debugging Plants");
        board[2][2].setPlant(new Peashooter(board[2][2]));
        board[2][2].getPlant().setGameEventListener(this);             //FOR DEBUGGING
        board[2][4].addZombie(new NormalZombie(board[2][4]));
        board[2][4].getZombies().get(0).setGameEventListener(this);          //FOR DEBUGGING

        gameBoard = new GameBoard(board);               // It does not about the level, it automatically adjusts!
        gameBoardGUI = new GameBoardGUI(board,level,this);
    }

    private void configureLevel() {     // This sets the parameters with what makes each level different!
        switch (level) {
            case 1: ROWS = 5;
                    COLS = 9;
                    waveLimit = 5;
                    sun = 150;
                    break;
            case 2: ROWS = 6;
                    COLS = 10;
                    waveLimit = 7;
                    sun = 100;
                    break;
            case 3: ROWS = 7;
                    COLS = 13;
                    waveLimit = 9;
                    sun = 50;
                    break;
            default: System.out.println("Error: Configuration of Level Invalid!");
                    break;
        }
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
        int zType = rand.nextInt(5);
        // Choose random number from 0 to 4

    switch (zType) {
        case 0: z = new NormalZombie(spawnTile); break;
        case 1: z = new FlagZombie(spawnTile); break;
        case 2: z = new ConeheadZombie(spawnTile); break;
        case 3: z = new BucketHeadZombie(spawnTile); break;
        case 4: z = new PoleVaultingZombie(spawnTile); break;
        default: z = new NormalZombie(spawnTile); break;
    }


        // We can scale this further to add zombies!!!
        z.setGameEventListener(this);
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

        // Refresh GUI immediately when a new zombie appears
            gameBoardGUI.update(z.getPosition());
        
    }
 

    public void setSelectedPlantType(int plantType) {
        this.selectedPlant = plantType;
    }

    public void handleTileClick(int clicked_row, int clicked_column) {
        placePlant(clicked_row, clicked_column, selectedPlant);
        this.selectedPlant = 0;
    }

    private void placePlant(int clicked_row, int clicked_column, int selectedPlant) {

        // CHECKERS BELOW
        if (selectedPlant==0) return;
        if (!isValidPosition(clicked_row,clicked_column)) return;
        if (!isValidPurchase(selectedPlant)) return;          // These do not affect the global variable "sun." Primitive values passed to a method is only a local copy

        Tile tilePlant = board[clicked_row][clicked_column];

            switch (selectedPlant) {

            case 1:    // SUNFLOWER
                tilePlant.setPlant(new Sunflower(tilePlant));
                tilePlant.getPlant().setGameEventListener(this);
                break;

            case 2:     // PEASHOOTER
                tilePlant.setPlant(new Peashooter(tilePlant));
                tilePlant.getPlant().setGameEventListener(this);
                break;

            case 3:     // CHERRYBOMB
                tilePlant.setPlant(new Cherrybomb(tilePlant));
                tilePlant.getPlant().setGameEventListener(this);
                break;

            case 4:     // WALLNUT
                tilePlant.setPlant(new Wallnut(tilePlant));
                tilePlant.getPlant().setGameEventListener(this);
                break;
            case 5:     // POTATO MINE
                tilePlant.setPlant(new PotatoMine(tilePlant));
                tilePlant.getPlant().setGameEventListener(this);
                break;
            case 6:     // FREEZE PEA
                tilePlant.setPlant(new FreezePeashooter(tilePlant));
                tilePlant.getPlant().setGameEventListener(this);
                break;
            case 7:     // REMOVE PLANT
                if (tilePlant.hasPlant()) {
                    tilePlant.removePlant();
                    System.out.println("Plant removed!");
                }
                break;

            default:
                System.out.println("Unknown plant not yet indicated placePlant() function");
                break;
        }

            System.out.println("Placed " + tilePlant.getPlant().getClass().getSimpleName() +
                " at Row " + (clicked_row + 1) + ", Column " + (clicked_column + 1));

        // Refresh GUI right after placing or removing a plant
            gameBoardGUI.update(tilePlant);

    }

    private boolean isValidPosition(int r, int c) {                   // USED FOR placePlant() function

        if ((r >= 0 && r < ROWS && c >= FIRST_PLANTABLE_COLUMN && c < COLS && !board[r][c].isOccupied())) {
            return true;
        } else {
            System.out.println("Invalid location!");
            return false;
        }
    }

    private boolean isValidPurchase(int selectedPlant) {
        int cost;

        switch (selectedPlant) {
            case 1: cost = 50; break;   // Sunflower
            case 2: cost = 100; break;  // Peashooter
            case 3: cost = 150; break;  // Cherrybomb
            case 4: cost = 50; break;   // Wallnut
            case 5: cost = 25; break;   // PotatoMine
            case 6: cost = 175; break;  // FreezePeashooter
            case 7: cost = 0; break;    // Remove (free)
            default:
                System.out.println("Unknown Plant!");
                return false;
        }

        if (sun >= cost) {
            sun -= cost;
            return true;
        } else {
            System.out.println("Not enough sun!");
            return false;
        }
    }

      

    /**
     * Iterates through all Peashooters and attacks the first zombie within range.
     * Eliminates zombies with zero health after being hit.
     */
private void handleAllPlants() {

    for (int r = 0; r < ROWS; r++) {                                                        // ROW
        for (int c = 0; c < COLS; c++) {                                                    // COLUMN
            Plant p = board[r][c].getPlant();                                               // Get Plant per Tile
            
            if (p !=null)
                switch (p.getClass().getSimpleName()) {

                case "Peashooter": 
                //LOGIC HERE CALL ACTION
                    Peashooter ps = (Peashooter) p;
                    ps.shoot(board);
                    break;

                case "Cherrybomb": 
                    Cherrybomb cb = (Cherrybomb) p;
                    // Advance fuse timer and explode when ready
                    cb.tick(board);
                    break;
                
                case "Sunflower":
                    if (ticks % 2 == 0) {
                    Sunflower s = (Sunflower) p;
                    sun = s.action(sun);
                }
                    break;

                case "Wallnut": 
                    // Do nothing!
                    break;

                case "PotatoMine":
                    PotatoMine pm = (PotatoMine) p;
                    pm.armExplode();
                    break;
                case "FreezePeashooter":
                    FreezePeashooter fp = (FreezePeashooter) p;
                    fp.shoot(board);
                    break;

                default: 
                    System.out.println("Unknown Plant Type Error"); 
                    break;// Catcher
                }

        }
    }
}

    /**
     * Updates all active Cherrybombs each tick. When a bomb's fuse expires it explodes
     * damaging nearby zombies before removing itself from the board.
     */

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
            z.attack(board);                    // WE CAN CHANGE GAME LOGIC HERE TO ATTACK WITH COOL DOWN, IF NEEDED!
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
        //selectLevel();
        configureLevel();
        initializeBoard();
        System.out.println("=== Plants vs Zombies Console Game ===");
        System.out.println("Level: " + level);
        gameBoard.display();
        gameBoardGUI.InitializeBoard();

        ticks = 0;
        timer = new Timer(1000, e -> runTick()); // 1000ms = 1 second
        timer.start();
    }

    public void runTick() {
        System.out.println("Time: " + formatTime(ticks));
        System.out.println("Current Sun: " + sun);

                justSpawned.clear();                          // clear tracker for this tick
                dropSun();                                    // Drop the Sun
                spawnZombies();                               // Spawn Some Zombies
                handleAllPlants();                            // Handles all Plants

                ZombiesWin = moveZombies();                   // Returns True if Zombie reaches House
                PlantsWin = (ticks >= GAME_DURATION);         // Returns True if Time is up
                gameBoard.display();

        if (PlantsWin) {
            System.out.println("Time's up! Plants survived. Plants win!");
            timer.stop();
        }

        if (ZombiesWin) {                  
            System.out.println("A zombie reached your house! Game Over. Zombies win!");
            timer.stop();
        }

            ticks++;
    }


    @Override
    public void onZombieKilled(Zombie z) {
        Tile t = z.getPosition();
        t.removeZombie(z);
        System.out.println("Zombie at Row " + (t.getRow()+1) + " Col " + (t.getColumn()+1) + " died.");

        gameBoardGUI.update(t);
        System.out.println("GUI is done updating at Zombie Killed.");

    }

    @Override
    public void onPlantKilled(Plant p) {
        Tile t = p.getPosition();
        t.removePlant();
        System.out.println("Plant at Row " + (t.getRow()+1) + " Col " + (t.getColumn()+1) + " was destroyed.");

        gameBoardGUI.update(t);
        System.out.println("GUI is done updating at Plant Killed.");
    }

    @Override
    public void onZombieMove(Tile newTile, Tile currentTile, Zombie z) {

        currentTile.removeZombie(z);
        newTile.addZombie(z);
        z.setPosition(newTile);

        gameBoardGUI.update(currentTile);
        gameBoardGUI.update(newTile);
        System.out.println("GUI is done updating at Zombie Move.");

    }

}

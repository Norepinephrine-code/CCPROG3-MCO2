package controller;


import events.GameEventListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import model.plants.Plant;
import model.plants.Sunflower;
import model.tiles.Tile;
import model.zombies.Zombie;
import util.MusicPlayer;
import view.GameBoard;
import view.GameBoardGUI;

/*
 * 
  ░██████                                                   ░██████                           ░██                        ░██ ░██                     
 ░██   ░██                                                 ░██   ░██                          ░██                        ░██ ░██                     
░██         ░██████   ░█████████████   ░███████           ░██         ░███████  ░████████  ░████████ ░██░████  ░███████  ░██ ░██  ░███████  ░██░████ 
░██  █████       ░██  ░██   ░██   ░██ ░██    ░██          ░██        ░██    ░██ ░██    ░██    ░██    ░███     ░██    ░██ ░██ ░██ ░██    ░██ ░███     
░██     ██  ░███████  ░██   ░██   ░██ ░█████████          ░██        ░██    ░██ ░██    ░██    ░██    ░██      ░██    ░██ ░██ ░██ ░█████████ ░██      
 ░██  ░███ ░██   ░██  ░██   ░██   ░██ ░██                  ░██   ░██ ░██    ░██ ░██    ░██    ░██    ░██      ░██    ░██ ░██ ░██ ░██        ░██      
  ░█████░█  ░█████░██ ░██   ░██   ░██  ░███████             ░██████   ░███████  ░██    ░██     ░████ ░██       ░███████  ░██ ░██  ░███████  ░██      
                                                                                                                                                     
  This Java File centralizes all the controllers and contains the game values.
 * 
 */

/**
 * Central game controller that orchestrates all other controllers and manages
 * the overall game state.
 */
public class Game implements GameEventListener {

    private static int ROWS;
    private static int COLS;
    private static final int HOUSE_COLUMN = 0;
    private static final int FIRST_PLANTABLE_COLUMN = HOUSE_COLUMN + 1;
    private static final int GAME_DURATION = 180;
    private Tile[][] board;
    private GameBoard gameBoard;
    private GameBoardGUI gameBoardGUI;
    private Random rand;
    private int level;
    private int waveLimit;
    private int sun;
    private int ticks;
    private boolean ZombiesWin = false;
    private boolean PlantsWin = false;
    private Timer timer;
    private int selectedPlant = 0;
    private List<Zombie> justSpawned = new ArrayList<>();

    private PlantController plantController;
    private ZombieController zombieController;
    private TileClickController tileClickController;
    private MusicPlayer musicPlayer = new MusicPlayer();


    /**
     * Constructs a game instance configured for the specified level.
     *
     * @param level level number to load
     */
    public Game(int level) {
        this.level = level;
        this.rand = new Random();   // For Generation of Random Suns
    }



/*
 * 
  ░██████        ░███       ░███     ░███    ░██████████               ░██████      ░██████████      ░███       ░█████████     ░██████████
 ░██   ░██      ░██░██      ░████   ░████    ░██                      ░██   ░██         ░██         ░██░██      ░██     ░██        ░██    
░██            ░██  ░██     ░██░██ ░██░██    ░██                     ░██                ░██        ░██  ░██     ░██     ░██        ░██    
░██  █████    ░█████████    ░██ ░████ ░██    ░█████████               ░████████         ░██       ░█████████    ░█████████         ░██    
░██     ██    ░██    ░██    ░██  ░██  ░██    ░██                             ░██        ░██       ░██    ░██    ░██   ░██          ░██    
 ░██  ░███    ░██    ░██    ░██       ░██    ░██                      ░██   ░██         ░██       ░██    ░██    ░██    ░██         ░██    
  ░█████░█    ░██    ░██    ░██       ░██    ░██████████               ░██████          ░██       ░██    ░██    ░██     ░██        ░██    
                                                                                                                                          
    /**
     * Initializes the board, sets up controllers and begins the main game loop.
     */
    public void start() {
 
        String message = """
        This game was forged through sleepless nights, relentless debugging, and undying passion of
        Hadriel H. Ramos & Royce Vergara.

        It is a testament to what two minds can build with heart, grit, and the will to create something fun, chaotic, and alive.

        Welcome to Plants vs Zombies.

        BUILD VERSION: 5.0
        """;
        JOptionPane.showMessageDialog(null, message, "Message from the Authors", JOptionPane.PLAIN_MESSAGE);

        configureLevel();
        initializeBoard();
        musicPlayer.playLoop("resources/audio/background.wav");

        gameBoard.display();
        gameBoardGUI.InitializeBoard();

        ticks = 0;
        timer = new Timer(1000, e -> runTick()); // 1000ms = 1 second
        timer.start();
    }

    /**
     * Executes one tick of game logic updating plants, zombies and the GUI.
     */
    public void runTick() {

        displayState();
        ZombiesWin = zombieController.tick(ticks);    
        PlantsWin = plantController.tick(ticks);  
        gameBoard.display();

        if (PlantsWin) {
            System.out.println("Time's up! Plants survived. Plants win!");
            JOptionPane.showMessageDialog(null, "Time's up! Plants survived. Plants win!", "Plants Win!", JOptionPane.INFORMATION_MESSAGE);
            timer.stop();
            System.exit(0);
        }

        if (ZombiesWin) {                  
            System.out.println("A zombie reached your house! Game Over. Zombies win!");
            JOptionPane.showMessageDialog(null, "A zombie reached your house! Game Over. Zombies Win!", "Zombies Win!", JOptionPane.INFORMATION_MESSAGE);
            timer.stop();
            System.exit(0);
        }

        if (ticks < 30) { maybeDropRandomSun();}

        ticks++;
        gameBoardGUI.updateIndicators(sun, ticks);
    } 

/* ==============================================================================================================//

░██████████                                     ░██             ░██         ░██              ░██                                              
░██                                             ░██             ░██                          ░██                                              
░██         ░██    ░██  ░███████  ░████████  ░████████          ░██         ░██ ░███████  ░████████  ░███████  ░████████   ░███████  ░██░████ 
░█████████  ░██    ░██ ░██    ░██ ░██    ░██    ░██             ░██         ░██░██           ░██    ░██    ░██ ░██    ░██ ░██    ░██ ░███     
░██          ░██  ░██  ░█████████ ░██    ░██    ░██             ░██         ░██ ░███████     ░██    ░█████████ ░██    ░██ ░█████████ ░██      
░██           ░██░██   ░██        ░██    ░██    ░██             ░██         ░██       ░██    ░██    ░██        ░██    ░██ ░██        ░██      
░██████████    ░███     ░███████  ░██    ░██     ░████          ░██████████ ░██ ░███████      ░████  ░███████  ░██    ░██  ░███████  ░██      
                                                                                                                                              
/ This GameEventListener interfaces centralizes all the removal and placement of entities in the board. /

//===============================================================================================================*/

    /** {@inheritDoc} */
    @Override
    public void onZombieKilled(Zombie z) {
        Tile t = z.getPosition();
        t.removeZombie(z);
        System.out.println("Zombie at Row " + (t.getRow()+1) + " Col " + (t.getColumn()+1) + " died.");

        gameBoardGUI.update(t);
        System.out.println("GUI is done updating at Zombie Killed.");

    }

    /** {@inheritDoc} */
    @Override
    public void onPlantKilled(Plant p) {
        Tile t = p.getPosition();
        t.removePlant();
        System.out.println("Plant at Row " + (t.getRow()+1) + " Col " + (t.getColumn()+1) + " was destroyed.");

        gameBoardGUI.update(t);
        System.out.println("GUI is done updating at Plant Killed.");
    }

    /** {@inheritDoc} */
    @Override
    public void onPlantRemoved(Plant p) {
        Tile t = p.getPosition();
        t.removePlant();

        gameBoardGUI.update(t);
        System.out.println("GUI is done updating at removed plant.");
    }

    /** {@inheritDoc} */
    @Override
    public void onZombieMove(Tile newTile, Tile currentTile, Zombie z) {

        currentTile.removeZombie(z);
        newTile.addZombie(z);
        z.setPosition(newTile);

        gameBoardGUI.update(currentTile);
        gameBoardGUI.update(newTile);
        System.out.println("GUI is done updating at Zombie Move.");

    }

    /** {@inheritDoc} */
    @Override
    public void onZombieGenerated(Zombie z) {
        Tile t = z.getPosition();
        t.addZombie(z);
        gameBoardGUI.update(z.getPosition());
        System.out.println("GUI is done updating a generated zombie.");
    }

    /** {@inheritDoc} */
    @Override
    public void onSetPlant(Plant p) {
        gameBoardGUI.update(p.getPosition());
        System.out.println("GUI is done updating a planted plant");
    }

    /** {@inheritDoc} */
    @Override
    public void onCollectSunFromSunflower(Sunflower sf) {
        sun+=50;
        gameBoardGUI.update(sf.getPosition());
        System.out.println("GUI is done updating manual collection of sun.");
    }

    /** {@inheritDoc} */
    @Override
    public void onCollectSunFromTile(Tile tile) {
        sun+=50;
        tile.setSunDrop(false);
        gameBoardGUI.update(tile);
    }

    /** {@inheritDoc} */
    @Override
    public void onDroppedSun(Tile tile) {
        tile.setSunDrop(true);
        gameBoardGUI.update(tile);
        System.out.println("GUI is done updating sun drop!");
    }

    /** {@inheritDoc} */
    @Override
    public void onReadySun(Sunflower sf) {
        gameBoardGUI.update(sf.getPosition());
        System.out.println("GUI is done updating of available sun.");
    }

    /*
     * 
  ░██████   ░██████████ ░██████████░██████████░██████████ ░█████████           ░██      ░██████  ░██████████ ░██████████░██████████░██████████ ░█████████  
 ░██   ░██  ░██             ░██        ░██    ░██         ░██     ░██         ░██      ░██   ░██ ░██             ░██        ░██    ░██         ░██     ░██ 
░██         ░██             ░██        ░██    ░██         ░██     ░██        ░██      ░██        ░██             ░██        ░██    ░██         ░██     ░██ 
 ░████████  ░█████████      ░██        ░██    ░█████████  ░█████████        ░██       ░██  █████ ░█████████      ░██        ░██    ░█████████  ░█████████  
        ░██ ░██             ░██        ░██    ░██         ░██   ░██        ░██        ░██     ██ ░██             ░██        ░██    ░██         ░██   ░██   
 ░██   ░██  ░██             ░██        ░██    ░██         ░██    ░██      ░██          ░██  ░███ ░██             ░██        ░██    ░██         ░██    ░██  
  ░██████   ░██████████     ░██        ░██    ░██████████ ░██     ░██    ░██            ░█████░█ ░██████████     ░██        ░██    ░██████████ ░██     ░██ 
                                                                                                                                                           
        - Simple setter and getter for Selected Plant Type and Sun Variable, both of which dynamically changes with user input.
     */
    // ====================== Setters and Getters for global variables ===============================  //
        /** Sets the currently selected plant type from the GUI. */
        public void setSelectedPlantType(int plantType) { this.selectedPlant = plantType;}
        /** Updates the player's sun resource count. */
        public void setSun(int sun) {this.sun=sun;}
        /** @return type id of the selected plant */
        public int getSelectedPlantType() {return this.selectedPlant;}
        /** @return current available sun */
        public int getSun() {return this.sun;}
        /** @return number of rows of the board */
        public int getRow() {return this.ROWS;}
        /** @return number of columns of the board */
        public int getColumn() {return this.COLS;}
    //  ===============================================================================================  //

    /*
     * 
░███     ░███ ░██████████ ░██████████░██     ░██   ░██████   ░███████     ░██████   
░████   ░████ ░██             ░██    ░██     ░██  ░██   ░██  ░██   ░██   ░██   ░██  
░██░██ ░██░██ ░██             ░██    ░██     ░██ ░██     ░██ ░██    ░██ ░██         
░██ ░████ ░██ ░█████████      ░██    ░██████████ ░██     ░██ ░██    ░██  ░████████  
░██  ░██  ░██ ░██             ░██    ░██     ░██ ░██     ░██ ░██    ░██         ░██ 
░██       ░██ ░██             ░██    ░██     ░██  ░██   ░██  ░██   ░██   ░██   ░██  
░██       ░██ ░██████████     ░██    ░██     ░██   ░██████   ░███████     ░██████   
     
    - Compartmentalized methods
     */

    /**
     * Prints the current game time and sun count to the console.
     */
    public void displayState() {
        System.out.println("Time: " + formatTime(ticks));
        System.out.println("Current Sun: " + sun);
    }

    //  ===============================================================================================  //
    /**
     * Randomly drops a sun on the board during the early game.
     */
    private void maybeDropRandomSun() {
    int chance = rand.nextInt(100);  // 0 to 99

    if (chance < 20) { // 20% chance per second
            int r = rand.nextInt(ROWS);
            int c = rand.nextInt(COLS - 1) + 1; // skip column 0 (house)

            Tile tile = board[r][c];

            // Only drop if no plant/zombie sun is already there
            if (!tile.hasSunDrop()) {
                onDroppedSun(tile);
            }
        }
    }
    //  ===============================================================================================  //
    
    //  ===============================================================================================  //
    /**
     * Sets board dimensions and starting resources based on the chosen level.
     */
    private void configureLevel() {
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

    //  ===============================================================================================  //
    /**
     * Constructs the tile grid and initializes all controller instances.
     */
    private void initializeBoard() {
        board = new Tile[ROWS][COLS];
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                board[r][c] = new Tile(r, c);
            }
        }

        gameBoard = new GameBoard(board);               // It does not about the level, it automatically adjusts!
        gameBoardGUI = new GameBoardGUI(board,level,this);
        this.plantController = new PlantController(this.board, this, ROWS, COLS);
        this.zombieController = new ZombieController(this.board, this, this.waveLimit);
        this.tileClickController = new TileClickController(this, board, this, ROWS, COLS);

    }
    //  ===============================================================================================  //

    /**
     * Delegates tile click handling to the {@link TileClickController}.
     */
    public void handleTileClick(int clicked_row, int clicked_column) {
        tileClickController.action(clicked_row, clicked_column);
    }

    //  ===============================================================================================  //

    /**
     * Utility to convert tick counts to a mm:ss formatted string.
     */
    private static String formatTime(int ticks) {
        int minutes = ticks / 60;
        int seconds = ticks % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
    //  ===============================================================================================  //
}

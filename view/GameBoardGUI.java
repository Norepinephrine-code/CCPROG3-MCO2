package view;

import controller.Game;
import java.awt.*;
import javax.swing.*;
import model.plants.*;
import model.tiles.Tile;
import model.zombies.*;

/**
 * Swing based GUI that visually represents the game board and handles user
 * interactions such as clicking tiles and selecting plants.
 */
public class GameBoardGUI {
    private Tile[][] board;
    private int rows;
    private int cols;
    private int level;
    private JFrame frame;
    private TileLabel[][] cells;
    private Game game;

    private JLabel sunIndicator;
    private JLabel timeIndicator;

    /**
     * Constructs the GUI for the given board and starts rendering on the
     * Swing event thread.
     *
     * @param board underlying board of tiles
     * @param level current game level
     * @param game  controller driving the game logic
     */
    public GameBoardGUI(Tile[][] board, int level, Game game) {
        this.board = board;
        this.rows = board.length;
        this.cols = board[0].length;
        this.level = level;
        this.game = game;
        SwingUtilities.invokeLater(this::createAndShowGUI);
    }

    /**
     * Initializes all Swing components and lays out the board grid along with
     * control panels.
     */
    private void createAndShowGUI() {

        frame = new JFrame("Plants vs Zombies");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ImageIcon logo = load("resources/images/logo.png");
        frame.setIconImage(logo.getImage());



        //========================================TOP PANEL======================================/
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());

        String[] plantNames = {"Sunflower",        // 1
                                "Peashooter",      // 2
                                "Cherrybomb",      // 3
                                "Wallnut",         // 4
                                "PotatoMine",      // 5
                                "SnowPea",         // 6
                                "Remove"};         // 7

        for (int i = 0; i < plantNames.length; i++) {
            final int plantType = i + 1;                // Since our switch case is 1 based in Game
            JButton btn = new JButton(plantNames[i]);
            // Lambda Notation. This just says that in an event of a click, it calls the method with the parameters
            btn.addActionListener(e -> game.setSelectedPlantType(plantType));  
            topPanel.add(btn);
        }
        //========================================TOP PANEL=======================================/
       



        //========================================GAME BOARD======================================/
        frame.setLayout(new BorderLayout());
        JPanel centerGrid = new JPanel(new GridLayout(rows, cols));
        cells = new TileLabel[rows][cols];

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                final int row = r;
                final int col = c;

                TileLabel lbl = new TileLabel();                                // Our Tile
                lbl.setBorder(BorderFactory.createLineBorder(Color.BLACK));     // Black Border
                lbl.setPreferredSize(new Dimension(80, 80));       // Dimensions of the cell

                // This makes the first column (house), have a mouse listener as well.
                // That is why we have boundary checks at Game.java with isValidPosition().
                lbl.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        game.handleTileClick(row, col);
                    }
                });

                cells[r][c] = lbl;
                centerGrid.add(lbl);
            }
        }
        //========================================GAME BOARD======================================/




        //========================================BOTTOM PANEL=====================================/
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 40, 5));

        sunIndicator = new JLabel("Sun: Goodluck!");
        sunIndicator.setFont(new Font("Arial", Font.BOLD, 16));

        timeIndicator = new JLabel("Time: Goodluck!");
        timeIndicator.setFont(new Font("Arial", Font.BOLD, 16));

        bottomPanel.add(sunIndicator);
        bottomPanel.add(timeIndicator);
        //========================================BOTTOM PANEL=====================================/



        
        //=========================================================================================/
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(centerGrid, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);
        frame.pack();                               // Make the frame fit into the tiles we made
        frame.setLocationRelativeTo(null);        // Center the frame to the computer screen
        frame.setVisible(true);                   // Show window... I dont know why this has to be manual
        InitializeBoard();                          // TIME TO PAINT
        //=========================================================================================/

    }

    /**
     * Updates the status labels showing the player's current sun and elapsed
     * time.
     *
     * @param sun   current sun count
     * @param ticks elapsed game ticks
     */
    public void updateIndicators(int sun, int ticks) {
        int minutes = ticks / 60;
        int seconds = ticks % 60;

        String formatTime = String.format("%02d:%02d", minutes, seconds);

        sunIndicator.setText("Sun: " + sun);
        timeIndicator.setText("Time: " + formatTime);
    }


    /**
     * Refreshes every tile in the GUI so that the current state of the board
     * is accurately shown.
     */
    public void InitializeBoard() {
        if (cells == null) return;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                update(board[r][c]);
            }
        }

        frame.revalidate();
        frame.repaint();
    }

    /**
     * Updates the icons for a single tile to match its current contents.
     *
     * @param pos tile that changed state
     */
    public void update(Tile pos) {
        if (cells == null) return;

        int r = pos.getRow();
        int c = pos.getColumn();

        TileLabel lbl = cells[r][c];
        Tile tile = board[r][c];

        Icon plantIcon = null;
        Icon zombieIcon = null;
        Icon houseIcon = null;
        Icon background = null;

                    // FIRST COLUMN will always be Cement
        if (c == 0) {
            background = getBackgroundIcon("Cement");
            houseIcon = getHouseIcon();
        } else {
            
            // This code will alternate between Light and Dark for the tiles DEPENDING
            // on the level!
            switch (level) {
                case 1: background = (c!=0 && (r+c)%2==0) ? 
                                        getBackgroundIcon("Light Green"): // True
                                        getBackgroundIcon("Dark Green");  // False
                        break;
                case 2: background = (c!=0 && c==cols-1) ? 
                                        getBackgroundIcon("Grave Mud"):     // True
                                        ((r+c)%2==0)     ?                       // False
                                            getBackgroundIcon("Light Mud"): // True
                                            getBackgroundIcon("Dark Mud");  // False
                        break;
                case 3: background = (c!=0 && c==cols-1) ? 
                                        getBackgroundIcon("Cloud Frost"):     // True
                                        ((r+c)%2==0)     ?                       // False
                                            getBackgroundIcon("Light Frost"): // True
                                            getBackgroundIcon("Dark Frost");  // False
                        break;
            }
        }
                

        if (tile.hasZombies() && tile.hasPlant()) {
            background = getBackgroundIcon("Under Attack!");
        }

        if (tile.isOccupied()) {
            Plant p = tile.getPlant();
            plantIcon = getPlantIcon(p);
        }

        if (tile.getZombies().size() > 1) {     // If there is a zombie, check if there is 1 or multiple.
            zombieIcon = getZombieIcon();       // Overloading method, this method returns the multiple zombie image
        } else if (tile.hasZombies()) {
            Zombie z = tile.getZombies().get(0); 
            zombieIcon = getZombieIcon(z);      // Overloading method, this method returns one single zombie
        }

        if (tile.getPlant() instanceof Sunflower) {
            Sunflower sf = (Sunflower) tile.getPlant();
            if (sf.hasSun()) {
                background = getBackgroundIcon("Sun Ready!");
            }
        }

        if (tile.hasSunDrop()) {
            background = getBackgroundIcon("Sun Dropped!");
        }

        lbl.setIcons(background, houseIcon, plantIcon, zombieIcon);

        frame.revalidate();
        frame.repaint();
    }


    /**
     * Retrieves the icon image for the given plant instance.
     *
     * @param p plant to look up
     * @return scaled icon representing the plant
     */
    private Icon getPlantIcon(Plant p) {

        if (p instanceof Sunflower) return load("resources/images/plants/sunflower.png");
        if (p instanceof Peashooter) return load("resources/images/plants/peashooter.png");
        if (p instanceof FreezePeashooter) return load("resources/images/plants/freezepeashooter.png");
        if (p instanceof Cherrybomb) return load("resources/images/plants/cherrybomb.png");
        if (p instanceof PotatoMine) return load("resources/images/plants/potatomine.png");
        if (p instanceof Wallnut) return load("resources/images/plants/wallnut.png");

        return load("resources/images/plants/unknownPlant.png"); 
    }


    /**
     * Returns the icon image for a specific zombie type.
     *
     * @param z zombie whose image should be displayed
     * @return scaled icon of the zombie
     */
    private Icon getZombieIcon(Zombie z) {

        if (z instanceof NormalZombie) return load("resources/images/zombies/normal.png");
        if (z instanceof FlagZombie) return load("resources/images/zombies/flag.png");
        if (z instanceof ConeheadZombie) return load("resources/images/zombies/conehead.png");
        if (z instanceof BucketHeadZombie) return load("resources/images/zombies/buckethead.png");

        if (z instanceof PoleVaultingZombie) {      // State-Dependent Icon!
            PoleVaultingZombie polevault_zombie = (PoleVaultingZombie) z;
            if (polevault_zombie.hasJumped()==false) return load("resources/images/zombies/polevault.png");
            if (polevault_zombie.hasJumped()==true) return load("resources/images/zombies/polevault_jump.png");
        }

        return load("resources/images/zombies/unknownZombie.png");
    }

    /**
     * Icon used when multiple zombies occupy a single tile.
     *
     * @return generic multiple-zombie icon
     */
    private Icon getZombieIcon() {
        return load("resources/images/zombies/multiple.png");
    }

    /**
     * Icon representing the player's house / lawn mower.
     *
     * @return house icon image
     */
    private Icon getHouseIcon() {
        return load("resources/images/house/lawn_mower.png");
    }

    /**
     * Retrieves the background tile image by name.
     *
     * @param type descriptor of the background tile
     * @return background icon image
     */
    private Icon getBackgroundIcon(String type) {

        if (type.equals("Cement")) return load("resources/images/backgrounds/cement.png");
        if (type.equals("Light Green")) return load("resources/images/backgrounds/light_grass.png");
        if (type.equals("Dark Green")) return load("resources/images/backgrounds/dark_grass.png");

        if (type.equals("Light Mud")) return load("resources/images/backgrounds/light_mud.png");
        if (type.equals("Dark Mud")) return load("resources/images/backgrounds/dark_mud.png");
        if (type.equals("Grave Mud")) return load("resources/images/backgrounds/grave_mud.png");

        if (type.equals("Light Frost")) return load("resources/images/backgrounds/light_frost.png");
        if (type.equals("Dark Frost")) return load("resources/images/backgrounds/dark_frost.png");
        if (type.equals("Cloud Frost")) return load("resources/images/backgrounds/cloud_frost.png");



        if (type.equals("Under Attack!")) return load("resources/images/backgrounds/under_attack.png");
        if (type.equals("Sun Ready!")) return load("resources/images/backgrounds/sun_ready.png");
        if (type.equals("Sun Dropped!")) return load("resources/images/backgrounds/sun_dropped.png");
 

        return load("resources/images/backgrounds/missing_grass.png");   

    }


    /**
     * Loads and scales an image from the given path, returning a default icon
     * if the file cannot be found.
     *
     * @param path path to the image file
     * @return loaded and scaled {@link ImageIcon}
     */
    private ImageIcon load(String path) {
        java.io.File file = new java.io.File(path);
        if (file.exists()) {
            ImageIcon rawIcon = new ImageIcon(file.getAbsolutePath());
            Image scaledImage = rawIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        }

        System.out.println("ERROR: Failed Reading " + path + ". Displaying missing icon!");
        ImageIcon fallback = new ImageIcon("resources/images/missing.png");
        Image scaled = fallback.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }
}

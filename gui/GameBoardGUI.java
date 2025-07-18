package gui;

import java.awt.*;
import javax.swing.*;
import plants.*;
import tiles.Tile;
import zombies.*;

public class GameBoardGUI {
    private Tile[][] board;
    private int rows;
    private int cols;
    private int level;
    private JFrame frame;
    private TileLabel[][] cells;

    public GameBoardGUI(Tile[][] board, int level) {
        this.board = board;
        this.rows = board.length;
        this.cols = board[0].length;
        this.level = level;
        SwingUtilities.invokeLater(this::createAndShowGUI);
    }

    private void createAndShowGUI() {
        frame = new JFrame("Plants vs Zombies GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(rows, cols));

        cells = new TileLabel[rows][cols];
        for (int r = 0; r < rows; r++) {                                       // Iterate through the entire tiles
            for (int c = 0; c < cols; c++) {
                TileLabel lbl = new TileLabel();                               // Declare our Tile GUI
                lbl.setBorder(BorderFactory.createLineBorder(Color.BLACK));    // Sets the borders
                lbl.setPreferredSize(new Dimension(80, 80));      // Sets the size of the borders
                cells[r][c] = lbl;                                             // Each cell is a TileLabel lbl
                frame.add(lbl);                                                // Add that to the frame. I dont know why Java Swing has to make this manual
            }
        }

        frame.pack();                               // Make the frame fit into the tiles we made
        frame.setLocationRelativeTo(null);        // Center the frame to the computer screen
        frame.setVisible(true);                   // Show window... I dont know why this has to be manual
        update();                                   // TIME TO PAINT
    }

    /** Refreshes the display to reflect the current board state. */
    public void update() {
        if (cells == null) return;

            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {                        //
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
                        
                        // This code will alternate between Light and Dark for the tiles
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

                    if (tile.hasZombies()) {
                        // only shows the first zombie for now here. 
                        // We can work on this later to add layering zombies
                        Zombie z = tile.getZombies().get(0); 
                        zombieIcon = getZombieIcon(z);
                    }

                    lbl.setIcons(background, houseIcon, plantIcon, zombieIcon);
                }
            }
            frame.revalidate();
            frame.repaint();

    }

    private Icon getPlantIcon(Plant p) {
        if (p instanceof Sunflower) return load("images/plants/sunflower.png");
        if (p instanceof Peashooter) return load("images/plants/peashooter.png");
        if (p instanceof FreezePeashooter) return load("images/plants/freezepeashooter.png");
        if (p instanceof Cherrybomb) return load("images/plants/cherrybomb.png");
        if (p instanceof PotatoMine) return load("images/plants/potatomine.png");
        if (p instanceof Wallnut) return load("images/plants/wallnut.png");
        return load("images/plants/unknownPlant.png"); 
    }


    private Icon getZombieIcon(Zombie z) {
        if (z instanceof NormalZombie) return load("images/zombies/normal.png");
        if (z instanceof FlagZombie) return load("images/zombies/flag.png");
        if (z instanceof ConeheadZombie) return load("images/zombies/conehead.png");
        if (z instanceof PoleVaultingZombie) return load("images/zombies/polevault.png");
        if (z instanceof BucketHeadZombie) return load("images/zombies/buckethead.png");
        return load("images/zombies/unknownZombie.png");
    }

    private Icon getHouseIcon() {
        return load("images/house/lawn_mower.png");
    }

    private Icon getBackgroundIcon(String type) {
        if (type.equals("Cement")) return load("images/backgrounds/cement.png");
        if (type.equals("Light Green")) return load("images/backgrounds/light_grass.png");
        if (type.equals("Dark Green")) return load("images/backgrounds/dark_grass.png");

        if (type.equals("Light Mud")) return load("images/backgrounds/light_mud.png");
        if (type.equals("Dark Mud")) return load("images/backgrounds/dark_mud.png");
        if (type.equals("Grave Mud")) return load("images/backgrounds/grave_mud.png");

        if (type.equals("Light Frost")) return load("images/backgrounds/light_frost.png");
        if (type.equals("Dark Frost")) return load("images/backgrounds/dark_frost.png");
        if (type.equals("Cloud Frost")) return load("images/backgrounds/cloud_frost.png");



        if (type.equals("Under Attack!")) return load("images/backgrounds/under_attack.png");


        return load("images/backgrounds/missing_grass.png");   

    }


    private ImageIcon load(String path) {
        java.io.File file = new java.io.File(path);
        if (file.exists()) {
            ImageIcon rawIcon = new ImageIcon(file.getAbsolutePath());
            Image scaledImage = rawIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        }

        System.out.println("ERROR: Failed Reading " + path + ". Displaying missing icon!");
        ImageIcon fallback = new ImageIcon("images/missing.png");
        Image scaled = fallback.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }
}

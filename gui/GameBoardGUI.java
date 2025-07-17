package gui;

import javax.swing.*;
import java.awt.*;
import plants.*;
import tiles.Tile;
import zombies.*;

/**
 * Very lightweight Swing based representation of the game board. It simply
 * mirrors the current board state using text or icons if images are available.
 */
public class GameBoardGUI {
    private Tile[][] board;
    private int rows;
    private int cols;
    private JFrame frame;
    private JLabel[][] cells;

    /**
     * Creates and shows a window representing the game board.
     * @param board backing board model
     */
    public GameBoardGUI(Tile[][] board) {
        this.board = board;
        this.rows = board.length;
        this.cols = board[0].length;
        SwingUtilities.invokeLater(this::createAndShowGUI);
    }

    private void createAndShowGUI() {
        frame = new JFrame("Plants vs Zombies GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(rows, cols));

        cells = new JLabel[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                JLabel lbl = new JLabel("", SwingConstants.CENTER);
                lbl.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                lbl.setPreferredSize(new Dimension(80, 80));
                cells[r][c] = lbl;
                frame.add(lbl);
            }
        }

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        update();
    }

    /** Refreshes the display to reflect the current board state. */
    public void update() {
        if (cells == null) return;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                JLabel lbl = cells[r][c];
                Tile tile = board[r][c];
                String text = "";
                Icon icon = null;

                if (c == 0) {
                    text = "H"; // house column
                } else if (tile.isOccupied()) {
                    Plant p = tile.getPlant();
                    icon = getPlantIcon(p);
                    if (icon == null) text = getPlantText(p);
                } else if (tile.hasZombies()) {
                    Zombie z = tile.getZombies().get(0);
                    icon = getZombieIcon(z);
                    if (icon == null) text = getZombieText(z);
                }

                lbl.setText(text);
                lbl.setIcon(icon);
            }
        }
    }

    private Icon getPlantIcon(Plant p) {
        if (p instanceof Sunflower)
            return load("images/plants/sunflower.png");
        if (p instanceof Peashooter)
            return load("images/plants/peashooter.png");
        if (p instanceof FreezePeashooter)
            return load("images/plants/freezepeashooter.png");
        if (p instanceof Cherrybomb)
            return load("images/plants/cherrybomb.png");
        if (p instanceof PotatoMine)
            return load("images/plants/potatomine.png");
        if (p instanceof Wallnut)
            return load("images/plants/wallnut.png");
        return null;
    }

    private String getPlantText(Plant p) {
        if (p instanceof Sunflower) return "S";
        if (p instanceof Peashooter) return "P";
        if (p instanceof FreezePeashooter) return "F";
        if (p instanceof Cherrybomb) return "C";
        if (p instanceof PotatoMine) return "M";
        if (p instanceof Wallnut) return "W";
        return "?";
    }

    private Icon getZombieIcon(Zombie z) {
        if (z instanceof NormalZombie)
            return load("images/zombies/normal.png");
        if (z instanceof FlagZombie)
            return load("images/zombies/flag.png");
        if (z instanceof ConeheadZombie)
            return load("images/zombies/conehead.png");
        if (z instanceof PoleVaultingZombie)
            return load("images/zombies/polevault.png");
        if (z instanceof BucketHeadZombie)
            return load("images/zombies/buckethead.png");
        return null;
    }

    private String getZombieText(Zombie z) {
        if (z instanceof NormalZombie) return "ZN";
        if (z instanceof FlagZombie) return "ZF";
        if (z instanceof ConeheadZombie) return "ZC";
        if (z instanceof PoleVaultingZombie) return "ZP";
        if (z instanceof BucketHeadZombie) return "ZB";
        return "?";
    }

    private ImageIcon load(String path) {
        java.net.URL url = getClass().getClassLoader().getResource(path);
        if (url != null) {
            return new ImageIcon(url);
        }
        java.io.File file = new java.io.File(path);
        if (file.exists()) {
            return new ImageIcon(file.getAbsolutePath());
        }
        return null;
    }
}

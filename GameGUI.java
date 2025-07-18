import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import plants.*;
import tiles.Tile;
import zombies.*;
import gui.TileLabel;

/** Simple Swing GUI composed of control, board, and status panels. */
public class GameGUI {
    private final SwingGame game;
    private final Tile[][] board;
    private final int rows;
    private final int cols;
    private final int level;
    private JFrame frame;
    private TileLabel[][] cells;
    private JLabel sunLabel;
    private JLabel timeLabel;

    public GameGUI(SwingGame game, Tile[][] board, int level) {
        this.game = game;
        this.board = board;
        this.rows = board.length;
        this.cols = board[0].length;
        this.level = level;
        SwingUtilities.invokeLater(this::createAndShowGUI);
    }

    private void createAndShowGUI() {
        frame = new JFrame("Plants vs Zombies");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Control panel with plant buttons
        JPanel control = new JPanel();
        String[] names = {"\u2600 Sunflower", "\uD83D\uDD2B Peashooter",
                "\uD83D\uDCA3 Cherrybomb", "\uD83D\uDEA7 Wallnut",
                "\uD83E\uDD54 PotatoMine", "\u2744 FreezePea"};
        PlantType[] types = {PlantType.SUNFLOWER, PlantType.PEASHOOTER,
                PlantType.CHERRYBOMB, PlantType.WALLNUT,
                PlantType.POTATOMINE, PlantType.FREEZEPEASHOOTER};
        for (int i = 0; i < names.length; i++) {
            JButton b = new JButton(names[i]);
            PlantType t = types[i];
            b.addActionListener(e -> game.setSelectedPlant(t));
            control.add(b);
        }
        frame.add(control, BorderLayout.NORTH);

        // Board panel
        JPanel boardPanel = new JPanel(new GridLayout(rows, cols));
        cells = new TileLabel[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                TileLabel lbl = new TileLabel();
                final int rr = r, cc = c;
                lbl.setPreferredSize(new Dimension(80, 80));
                lbl.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                lbl.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        game.queuePlanting(rr, cc);
                    }
                });
                cells[r][c] = lbl;
                boardPanel.add(lbl);
            }
        }
        frame.add(boardPanel, BorderLayout.CENTER);

        // Status panel
        JPanel status = new JPanel();
        sunLabel = new JLabel("Sun: 0");
        timeLabel = new JLabel("Time: 0");
        status.add(sunLabel);
        status.add(timeLabel);
        frame.add(status, BorderLayout.SOUTH);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        update();
    }

    /** Refresh board and status labels. Must be called on EDT. */
    public void update() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Tile tile = board[r][c];
                Icon plantIcon = null;
                Icon zombieIcon = null;
                Icon houseIcon = null;
                Icon background = null;

                if (c == 0) {
                    background = getBackgroundIcon("Cement");
                    houseIcon = getHouseIcon();
                } else {
                    switch (level) {
                        case 1:
                            background = (c != 0 && (r + c) % 2 == 0)
                                    ? getBackgroundIcon("Light Green")
                                    : getBackgroundIcon("Dark Green");
                            break;
                        case 2:
                            background = (c != 0 && c == cols - 1)
                                    ? getBackgroundIcon("Grave Mud")
                                    : ((r + c) % 2 == 0)
                                    ? getBackgroundIcon("Light Mud")
                                    : getBackgroundIcon("Dark Mud");
                            break;
                        default:
                            background = (c != 0 && c == cols - 1)
                                    ? getBackgroundIcon("Cloud Frost")
                                    : ((r + c) % 2 == 0)
                                    ? getBackgroundIcon("Light Frost")
                                    : getBackgroundIcon("Dark Frost");
                            break;
                    }
                }

                if (tile.hasZombies() && tile.hasPlant()) {
                    background = getBackgroundIcon("Under Attack!");
                }
                if (tile.isOccupied()) {
                    plantIcon = getPlantIcon(tile.getPlant());
                }
                if (tile.hasZombies()) {
                    Zombie z = tile.getZombies().get(0);
                    zombieIcon = getZombieIcon(z);
                }

                cells[r][c].setIcons(background, houseIcon, plantIcon, zombieIcon);
            }
        }
        sunLabel.setText("Sun: " + game.getSun());
        timeLabel.setText("Time: " + SwingGame.formatTime(game.getTicks()));
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
        switch (type) {
            case "Cement":
                return load("images/backgrounds/cement.png");
            case "Light Green":
                return load("images/backgrounds/light_grass.png");
            case "Dark Green":
                return load("images/backgrounds/dark_grass.png");
            case "Light Mud":
                return load("images/backgrounds/light_mud.png");
            case "Dark Mud":
                return load("images/backgrounds/dark_mud.png");
            case "Grave Mud":
                return load("images/backgrounds/grave_mud.png");
            case "Light Frost":
                return load("images/backgrounds/light_frost.png");
            case "Dark Frost":
                return load("images/backgrounds/dark_frost.png");
            case "Cloud Frost":
                return load("images/backgrounds/cloud_frost.png");
            case "Under Attack!":
                return load("images/backgrounds/under_attack.png");
            default:
                return load("images/backgrounds/missing_grass.png");
        }
    }

    private ImageIcon load(String path) {
        java.io.File file = new java.io.File(path);
        if (file.exists()) {
            ImageIcon rawIcon = new ImageIcon(file.getAbsolutePath());
            Image scaledImage = rawIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        }
        ImageIcon fallback = new ImageIcon("images/missing.png");
        Image scaled = fallback.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }
}

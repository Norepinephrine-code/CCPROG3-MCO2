import gui.TileLabel;
import zombies.*;
import plants.*;
import tiles.Tile;
import java.util.*;
import java.util.concurrent.*;
import javax.swing.SwingUtilities;

/** Tick-based Plants vs Zombies game running at 1 second per tick. */
public class SwingGame {
    private static int ROWS;
    private static int COLS;
    private static final int HOUSE_COLUMN = 0;
    private static final int FIRST_PLANTABLE_COLUMN = HOUSE_COLUMN + 1;
    private static final int GAME_DURATION = 180;

    private Tile[][] board;
    private GameGUI gui;
    private Random rand = new Random();

    private int level;
    private int waveLimit;
    private int sun;
    private int ticks;
    private List<Zombie> justSpawned = new ArrayList<>();
    private PlantType selectedPlant;
    private final ConcurrentLinkedQueue<PlantingRequest> plantQueue = new ConcurrentLinkedQueue<>();
    private ScheduledExecutorService exec;

    public SwingGame(int level) {
        this.level = level;
    }

    public int getSun() { return sun; }
    public int getTicks() { return ticks; }

    public void setSelectedPlant(PlantType type) {
        this.selectedPlant = type;
    }

    public void queuePlanting(int row, int col) {
        if (selectedPlant != null) {
            plantQueue.add(new PlantingRequest(row, col, selectedPlant));
        }
    }

    public static String formatTime(int t) {
        int m = t / 60;
        int s = t % 60;
        return String.format("%02d:%02d", m, s);
    }

    public void start() {
        configureLevel();
        initializeBoard();
        gui = new GameGUI(this, board, level);
        exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleWithFixedDelay(this::tick, 0, 1, TimeUnit.SECONDS);
    }

    private void configureLevel() {
        switch (level) {
            case 1:
                ROWS = 5; COLS = 9; waveLimit = 5; sun = 150; break;
            case 2:
                ROWS = 6; COLS = 10; waveLimit = 7; sun = 100; break;
            case 3:
                ROWS = 7; COLS = 13; waveLimit = 9; sun = 50; break;
            default:
                ROWS = 5; COLS = 9; waveLimit = 5; sun = 150; break;
        }
    }

    private void initializeBoard() {
        board = new Tile[ROWS][COLS];
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                board[r][c] = new Tile(r, c);
            }
        }
    }

    private void tick() {
        try {
            processPlantRequests();
            dropSun();
            spawnZombies();
            handleAllPlants();
            boolean lost = moveZombies();
            ticks++;
            SwingUtilities.invokeLater(() -> gui.update());
            if (lost || ticks >= GAME_DURATION) {
                exec.shutdown();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processPlantRequests() {
        PlantingRequest req;
        while ((req = plantQueue.poll()) != null) {
            int r = req.row;
            int c = req.col;
            if (r < 0 || r >= ROWS || c < FIRST_PLANTABLE_COLUMN || c >= COLS)
                continue;
            Tile tile = board[r][c];
            if (tile.isOccupied()) continue;
            if (sun < req.type.cost) continue;
            sun -= req.type.cost;
            switch (req.type) {
                case SUNFLOWER:
                    tile.setPlant(new Sunflower(tile)); break;
                case PEASHOOTER:
                    tile.setPlant(new Peashooter(tile)); break;
                case CHERRYBOMB:
                    tile.setPlant(new Cherrybomb(tile)); break;
                case WALLNUT:
                    tile.setPlant(new Wallnut(tile)); break;
                case POTATOMINE:
                    tile.setPlant(new PotatoMine(tile)); break;
                case FREEZEPEASHOOTER:
                    tile.setPlant(new FreezePeashooter(tile)); break;
            }
        }
    }

    private void dropSun() {
        if (ticks % 5 == 0) {
            sun += 25;
        }
    }

    private void spawnZombies() {
        if (ticks >= 30 && ticks <= 80 && ticks % 10 == 0) {
            spawnSingleZombie();
        } else if (ticks >= 81 && ticks <= 140 && ticks % 5 == 0) {
            spawnSingleZombie();
        } else if (ticks >= 141 && ticks <= 170 && ticks % 3 == 0) {
            spawnSingleZombie();
        } else if (ticks == 171) {
            for (int i = 0; i < waveLimit; i++) {
                spawnSingleZombie();
            }
        }
    }

    private void spawnSingleZombie() {
        int row = rand.nextInt(ROWS);
        Tile spawnTile = board[row][COLS - 1];
        Zombie z;
        switch (rand.nextInt(5)) {
            case 0: z = new NormalZombie(spawnTile); break;
            case 1: z = new FlagZombie(spawnTile); break;
            case 2: z = new ConeheadZombie(spawnTile); break;
            case 3: z = new BucketHeadZombie(spawnTile); break;
            default: z = new PoleVaultingZombie(spawnTile); break;
        }
        spawnTile.addZombie(z);
        justSpawned.add(z);
    }

    private void handleAllPlants() {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                Plant p = board[r][c].getPlant();
                if (p == null) continue;
                switch (p.getClass().getSimpleName()) {
                    case "Peashooter":
                        ((Peashooter)p).shoot(board); break;
                    case "FreezePeashooter":
                        ((FreezePeashooter)p).shoot(board); break;
                    case "Cherrybomb":
                        ((Cherrybomb)p).tick(board); break;
                    case "Sunflower":
                        if (ticks % 2 == 0) sun = ((Sunflower)p).action(sun); break;
                    case "PotatoMine":
                        ((PotatoMine)p).armExplode(); break;
                    default:
                        break;
                }
            }
        }
    }

    private boolean moveZombies() {
        List<Zombie> moving = new ArrayList<>();
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                moving.addAll(board[r][c].getZombies());
            }
        }
        for (Zombie z : moving) {
            z.attack(board);
            Tile curr = z.getPosition();
            if (curr.getPlant() == null) {
                if (!justSpawned.contains(z) && ticks % z.getSpeed() == 0) {
                    z.move(board);
                }
            }
            if (z.getPosition().getColumn() == 0) {
                return true;
            }
        }
        justSpawned.clear();
        return false;
    }
}

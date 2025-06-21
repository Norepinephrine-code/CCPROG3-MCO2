
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import tiles.Tile;
import plants.Plant;
import plants.Sunflower;
import plants.Peashooter;
import zombies.Zombie;
import zombies.NormalZombie;
import zombies.FlagZombie;
import zombies.ConeheadZombie;

public class Game {
    private static final int ROWS = 5;
    private static final int COLS = 9;
    private static final int GAME_DURATION = 180; // ticks

    private Tile[][] board;
    private GameBoard gameBoard;
    private Scanner scanner;
    private Random rand;

    private int level;
    private int sun;
    private int ticks;

    public Game() {
        scanner = new Scanner(System.in);
        rand = new Random();
    }

    private void initializeBoard() {
        board = new Tile[ROWS][COLS];
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                board[r][c] = new Tile(r, c);
            }
        }
        gameBoard = new GameBoard(board);
    }

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

    private void dropSun() {
        if (ticks % 5 == 0) {
            sun += 25;
            System.out.println("Sun dropped! Current sun: " + sun);
        }
    }

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
            System.out.println(
                    "Zombie appeared in Row " + (row + 1) + ", Column " + COLS
                            + " | Type: " + z.getClass().getSimpleName()
                            + " | Health=" + z.getHealth() + ", Speed=" + z.getSpeed());
        }
    }

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

    private void placePlant() {
        System.out.print("Place plant? (1-Sunflower, 2-Peashooter, 0-None):");
        int choice = -1;
        try {
            choice = Integer.parseInt(scanner.nextLine());
        } catch (Exception e) {
            choice = -1;
        }

        if (choice == 1 && sun >= 50) {
            System.out.print("Enter row (1-" + ROWS + ") and column (1-" + COLS + "): ");
            int r = scanner.nextInt() - 1;
            int c = scanner.nextInt() - 1;
            scanner.nextLine();
            if (r >= 0 && r < ROWS && c >= 0 && c < COLS && !board[r][c].isOccupied()) {
                board[r][c].setPlant(new Sunflower(board[r][c]));
                sun -= 50;
                System.out.println("Placed Sunflower at Row " + (r + 1) + " Column " + (c + 1));
            } else {
                System.out.println("Invalid location.");
            }
        } else if (choice == 2 && sun >= 100) {
            System.out.print("Enter row (1-" + ROWS + ") and column (1-" + COLS + "): ");
            int r = scanner.nextInt() - 1;
            int c = scanner.nextInt() - 1;
            scanner.nextLine();
            if (r >= 0 && r < ROWS && c >= 0 && c < COLS && !board[r][c].isOccupied()) {
                board[r][c].setPlant(new Peashooter(board[r][c]));
                sun -= 100;
                System.out.println("Placed Peashooter at Row " + (r + 1) + " Column " + (c + 1));
            } else {
                System.out.println("Invalid location.");
            }
        }
    }

    private void handlePeashooters() {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                Plant p = board[r][c].getPlant();
                if (p instanceof Peashooter) {
                    for (int zc = c + 1; zc < COLS && zc <= c + p.getRange(); zc++) {
                        if (board[r][zc].hasZombies()) {
                            List<Zombie> zs = new ArrayList<>(board[r][zc].getZombies());
                            for (Zombie z : zs) {
                                p.action(z);
                                if (!z.isAlive()) {
                                    board[r][zc].removeZombie(z);
                                    System.out.println(
                                            "Zombie at Row " + (r + 1) + " Col " + (zc + 1) + " died.");
                                }
                            }
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * Moves zombies, handles attacks and checks for game over.
     *
     * @return {@code true} if a zombie reached the house
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
            if (ticks % z.getSpeed() == 0) {
                z.move(board);
            }
            if (z.getPosition().getColumn() == 0) {
                System.out.println("A zombie reached your house! Game Over. Zombies win!");
                return true;
            }
        }
        return false;
    }

    private static String formatTime(int ticks) {
        int minutes = ticks / 60;
        int seconds = ticks % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    public void start() {
        initializeBoard();
        selectLevel();

        sun = 150;
        ticks = 0;

        System.out.println("=== Plants vs Zombies Console Game ===");
        System.out.println("Level: " + level);

        while (ticks <= GAME_DURATION) {
            System.out.println("Time: " + formatTime(ticks));

            dropSun();
            spawnZombies();
            handleSunflowers();
            System.out.println("Current Sun: " + sun);
            placePlant();
            handlePeashooters();
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

    

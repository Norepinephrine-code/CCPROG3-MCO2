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

/**
 * Entry point for the console-based Plants vs Zombies game.
 * Handles initialization of the board, user interaction and
 * the main game loop.
 */
public class Driver {

    private static final int ROWS = 5;
    private static final int COLS = 9;
    private static final int GAME_DURATION = 180; // ticks

    /**
     * Converts the given tick count into a human readable time string.
     *
     * @param ticks number of elapsed ticks
     * @return time in the format mm:ss
     */
    private static String formatTime(int ticks) {
        int minutes = ticks / 60;
        int seconds = ticks % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    /**
     * Launches the game and processes player input each tick.
     *
     * @param args command line arguments (unused)
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // initialize playing field
        Tile[][] board = new Tile[ROWS][COLS];
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                board[r][c] = new Tile(r, c);
            }
        }

        GameBoard gameBoard = new GameBoard(board);

        // ask player which difficulty level to play
        System.out.print("Select Level (1, 2, or 3): ");
        int level = 1;
        try {
            level = Integer.parseInt(scanner.nextLine());
        } catch (Exception e) {
            level = 1;
        }
        if (level < 1) level = 1;
        if (level > 3) level = 3;

        int sun = 150; // starting sun
        int ticks = 0;
        Random rand = new Random();

        System.out.println("=== Plants vs Zombies Console Game ===");
        System.out.println("Level: " + level);

        // main game loop runs until the duration elapses
        while (ticks <= GAME_DURATION) {
            System.out.println("Time: " + formatTime(ticks));

            // ambient sun drop every 5 ticks
            if (ticks % 5 == 0) {
                sun += 25;
                System.out.println("Sun dropped! Current sun: " + sun);
            }

            // determine if a zombie should spawn this tick
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
                System.out.println("Zombie appeared in Row " + (row + 1) + ", Column " + COLS
                        + " | Type: " + z.getClass().getSimpleName()
                        + " | Health=" + z.getHealth() + ", Speed=" + z.getSpeed());
            }

            // allow sunflowers to generate sun every other tick
            for (int r = 0; r < ROWS; r++) {
                for (int c = 0; c < COLS; c++) {
                    Plant p = board[r][c].getPlant();
                    if (p instanceof Sunflower && ticks % 2 == 0) {
                        Sunflower s = (Sunflower) p;
                        sun = s.action(sun);
                    }
                }
            }

            System.out.println("Current Sun: " + sun);

            // prompt user for plant placement
            System.out.print("Place plant? (1-Sunflower, 2-Peashooter, 0-None): ");
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

            // peashooters look for zombies in range and attack
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
                                        System.out.println("Zombie at Row " + (r + 1) + " Col " + (zc + 1) + " died.");
                                    }
                                }
                                break;
                            }
                        }
                    }
                }
            }

            // gather all zombies so they can act this tick
            List<Zombie> movingZombies = new ArrayList<>();
            for (int r = 0; r < ROWS; r++) {
                for (int c = 0; c < COLS; c++) {
                    movingZombies.addAll(board[r][c].getZombies());
                }
            }

            // each zombie attacks then moves depending on its speed
            for (Zombie z : movingZombies) {
                z.attack();
                if (ticks % z.getSpeed() == 0) {
                    z.move(board);
                }
                if (z.getPosition().getColumn() == 0) {
                    System.out.println("A zombie reached your house! Game Over. Zombies win!");
                    scanner.close();
                    return;
                }
            }

            // show updated board state
            gameBoard.display();

            // check if the game duration has been reached
            if (ticks == GAME_DURATION) {
                System.out.println("Time's up! Plants survived. Plants win!");
                scanner.close();
                return;
            }

            ticks++;
        }
    }
}

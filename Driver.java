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

/* In progress */

public class Driver {

    private static final int ROWS = 5;
    private static final int COLS = 9;

    private static String formatTime(int ticks) {
        int minutes = ticks / 60;
        int seconds = ticks % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Tile[][] board = new Tile[ROWS][COLS];
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                board[r][c] = new Tile(r, c);
            }
        }

        int sun = 150; // starting sun
        int ticks = 0;
        Random rand = new Random();

        while (true) {
            System.out.println("Time: " + formatTime(ticks));

            // Spawn a zombie every 5 ticks
            if (ticks % 5 == 0) {
                int row = rand.nextInt(ROWS);
                Tile spawnTile = board[row][COLS - 1];
                Zombie z = new NormalZombie(spawnTile);
                spawnTile.addZombie(z);
                System.out.println("Zombie appeared in Row " + row + ", Column " + (COLS - 1)
                        + " with Health=" + z.getHealth() + ", Speed=" + z.getSpeed());
            }

            // Sunflowers generate sun
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

            // Ask player to place a plant
            System.out.print("Place plant? (1-Sunflower, 2-Peashooter, 0-None): ");
            int choice = -1;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                choice = -1;
            }
            if (choice == 1 && sun >= 50) {
                System.out.print("Enter row and column: ");
                int r = scanner.nextInt();
                int c = scanner.nextInt();
                scanner.nextLine();
                if (r >= 0 && r < ROWS && c >= 0 && c < COLS && !board[r][c].isOccupied()) {
                    board[r][c].setPlant(new Sunflower(board[r][c]));
                    sun -= 50;
                    System.out.println("Placed Sunflower at Row " + r + " Column " + c);
                } else {
                    System.out.println("Invalid location.");
                }
            } else if (choice == 2 && sun >= 100) {
                System.out.print("Enter row and column: ");
                int r = scanner.nextInt();
                int c = scanner.nextInt();
                scanner.nextLine();
                if (r >= 0 && r < ROWS && c >= 0 && c < COLS && !board[r][c].isOccupied()) {
                    board[r][c].setPlant(new Peashooter(board[r][c]));
                    sun -= 100;
                    System.out.println("Placed Peashooter at Row " + r + " Column " + c);
                } else {
                    System.out.println("Invalid location.");
                }
            }

            // Plants attack
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
                                        System.out.println("Zombie at Row " + r + " Col " + zc + " died.");
                                    }
                                }
                                break;
                            }
                        }
                    }
                }
            }

            // Zombies attack and move
            List<Zombie> movingZombies = new ArrayList<>();
            for (int r = 0; r < ROWS; r++) {
                for (int c = 0; c < COLS; c++) {
                    movingZombies.addAll(board[r][c].getZombies());
                }
            }

            for (Zombie z : movingZombies) {
                z.attack();
                z.move(board);
                if (z.getPosition().getColumn() == 0) {
                    System.out.println("A zombie reached your house! Game Over.");
                    scanner.close();
                    return;
                }
            }

            ticks++;
            System.out.println();
        }
    }
}

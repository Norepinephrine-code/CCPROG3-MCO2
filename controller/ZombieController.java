package controller;

import events.GameEventListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JOptionPane;
import model.tiles.Tile;
import model.zombies.*;

/**
 * Handles spawning and movement logic for zombies.
 */
public class ZombieController {

    private final Tile[][] board;
    private final GameEventListener listener;
    private Random rand = new Random();
    private List<Zombie> justSpawned = new ArrayList<>();
    private final int waveLimit;

    public ZombieController(Tile[][] board, GameEventListener listener, int waveLimit) {
        this.board = board;
        this.listener = listener;
        this.waveLimit = waveLimit;
    }

    /**
     * Processes zombie behavior for the current tick.
     *
     * @param ticks current tick count
     * @return {@code true} if a zombie reached the house
     */
    public boolean tick(int ticks) {
        justSpawned.clear();
        spawnZombies(ticks);
        return moveZombies(ticks);
    }

    private void spawnZombies(int ticks) {
        if (ticks >= 30 && ticks <= 80 && ticks % 10 == 0) {
            spawnSingleZombie();
        } else if (ticks >= 81 && ticks <= 140 && ticks % 5 == 0) {
            spawnSingleZombie();
        } else if (ticks >= 141 && ticks <= 170 && ticks % 3 == 0) {
            spawnSingleZombie();
        } else if (ticks == 171) {
            JOptionPane.showMessageDialog(null, "Wave of Zombies Commencing!", "Alert!", JOptionPane.WARNING_MESSAGE);
            for (int waveCount = 0; waveCount < waveLimit; waveCount++) {
                spawnSingleZombie();
            }
        }
    }

    private void spawnSingleZombie() {
        int rows = board.length;
        int cols = board[0].length;
        int row = rand.nextInt(rows);
        Tile spawnTile = board[row][cols - 1];
        Zombie z;
        int zType = rand.nextInt(5);
        switch (zType) {
            case 0: z = new NormalZombie(spawnTile); break;
            case 1: z = new FlagZombie(spawnTile); break;
            case 2: z = new ConeheadZombie(spawnTile); break;
            case 3: z = new BucketHeadZombie(spawnTile); break;
            case 4: z = new PoleVaultingZombie(spawnTile); break;
            default: z = new NormalZombie(spawnTile); break;
        }
        z.setGameEventListener(listener);
        spawnTile.addZombie(z);
        justSpawned.add(z);
        System.out.println("Zombie appeared in Row " + (row + 1) + ", Column " + cols
                + " | Type: " + z.getClass().getSimpleName()
                + " | Health=" + z.getHealth() + ", Speed=" + z.getSpeed());

        listener.onZombieGenerated(z);              // Inform the Game that a zombie is generated

    }

    private boolean moveZombies(int ticks) {
        int rows = board.length;
        int cols = board[0].length;
        List<Zombie> movingZombies = new ArrayList<>();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                movingZombies.addAll(board[r][c].getZombies());
            }
        }

        for (Zombie z : movingZombies) {
            z.attack(board);
            Tile current = z.getPosition();
            if (current.getPlant() == null) {
                if (!justSpawned.contains(z) && ticks % z.getSpeed() == 0) {
                    z.move(board);
                }
            }
            if (z.getPosition().getColumn() == 0) {
                return true;
            }
        }
        return false;
    }
}
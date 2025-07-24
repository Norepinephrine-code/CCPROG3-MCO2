package model.plants;

import java.util.List;

import model.tiles.Tile;
import model.zombies.Zombie;

/**
 * Basic offensive plant that shoots peas at the nearest zombie in its row.
 */
public class Peashooter extends Plant {

    private static final int COOLDOWN_TICKS = 1;
    private int cooldownCounter = 0;

    /**
     * Creates a peashooter at the given tile.
     *
     * @param position tile where the plant is placed
     */
    public Peashooter(Tile position) {
        super(100, 4.5f, 15, 50, 9, 20, 2.0f, position);
    }

    /**
     * Scans the row for the first zombie within range and attacks it.
     *
     * @param board current game board
     */
    public void shoot(Tile[][] board) {

        int r = this.getPosition().getRow();
        int c = this.getPosition().getColumn();

        boolean hasAttacked = false;

        for (int zc = c; zc < board[0].length && zc <= c + this.getRange() && !hasAttacked; zc++) {
            if (board[r][zc].hasZombies()) {
                List<Zombie> zs = board[r][zc].getZombies();
                if (!zs.isEmpty()) {
                    Zombie target = zs.get(0);
                    this.action(target);
                    if (!target.isAlive()) {
                        listener.onZombieKilled(target);
                    }
                    hasAttacked = true;
                }
            }
        }

    }

    /**
     * Adds a simple cooldown mechanic on top of {@link Plant#action(Zombie)}.
     */
    @Override
    public int action(Zombie zombie) {
        if (cooldownCounter > 0) {
            cooldownCounter--;
            return zombie.getHealth();
        }

        int before = zombie.getHealth();
        int after = super.action(zombie);

        if (after != before) {
            cooldownCounter = COOLDOWN_TICKS;
        }

        return after;
    }
}

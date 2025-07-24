package model.plants;

import java.util.List;

import model.tiles.Tile;
import model.zombies.Zombie;

/**
 * Variant of the peashooter that freezes zombies it hits.
 */
public class FreezePeashooter extends Plant {

    private static final int COOLDOWN_TICKS = 1;
    private int cooldownCounter = 0;

    /**
     * Creates a freeze peashooter at the given position.
     *
     * @param position tile where the plant is placed
     */
    public FreezePeashooter(Tile position) {
        super(100, 4.5f, 15, 50, 9, 20, 2.0f, position);
    }

    /**
     * Shoots a projectile down the row freezing the first zombie hit.
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
                    //*************  */ DEATH CHECKER **********//
                    if (!target.isAlive()) {                    // IF DEAD KILL AND ANNOUNCE
                        listener.onZombieKilled(target);
                    } else {
                        target.freezeFor(5);              // IF NOT DEAD, FREEZE AND ANNOUNCE
                        System.out.println("Zombie at Row " + (r + 1) + " Col " + (zc + 1) + " freezed");
                    }

                    hasAttacked = true; 
                }
            }
        }
    }

    /** {@inheritDoc} */
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

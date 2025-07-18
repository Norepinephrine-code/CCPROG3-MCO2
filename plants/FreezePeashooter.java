package plants;

import java.util.List;
import tiles.Tile;
import zombies.Zombie;

public class FreezePeashooter extends Plant {

    private static final int COOLDOWN_TICKS = 1;
    private int cooldownCounter = 0;

    public FreezePeashooter(Tile position) {
        super(100, 4.5f, 15, 50, 9, 20, 2.0f, position);
    }

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
                        if (listener != null) listener.onZombieKilled(target);
                        System.out.println("Zombie at Row " + (r + 1) + " Col " + (zc + 1) + " died.");
                    } else {
                        target.freezeFor(5);              // IF NOT DEAD, FREEZE AND ANNOUNCE
                        System.out.println("Zombie at Row " + (r + 1) + " Col " + (zc + 1) + " freezed");
                    }

                    hasAttacked = true; 
                }
            }
        }
    }

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

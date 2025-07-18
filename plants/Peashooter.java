package plants;

import java.util.List;
import tiles.Tile;
import zombies.Zombie;

public class Peashooter extends Plant {

    private static final int COOLDOWN_TICKS = 1;
    private int cooldownCounter = 0;

    public Peashooter(Tile position) {
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
                    if (!target.isAlive()) {
                        listener.onZombieKilled(target);
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

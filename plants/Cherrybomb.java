package plants;

import java.util.ArrayList;
import java.util.List;

import tiles.Tile;
import zombies.Zombie;

/**
 * Explosive plant that damages all zombies in a 3x3 area around it.
 */
public class Cherrybomb extends Plant {

    /** Number of ticks before the cherrybomb explodes. */
    private int fuse;

    /**
     * Creates a Cherrybomb at the given tile.
     *
     * @param position tile where the bomb is placed
     */
    public Cherrybomb(Tile position){
        super(150,5.5f,1800,1000,1,1800,5.5f,position);
        this.fuse = 3; // explode after 3 ticks
    }

    /**
     * Triggers the cherrybomb explosion using the provided game board.
     *
     * @param board the current game board
     */
    public void action(Tile[][] board) {
        explode(board);
    }

    /**
     * Updates the fuse each tick and triggers explosion when it reaches zero.
     *
     * @param board the current game board used to locate zombies
     */
    public void tick(Tile[][] board) {
        if (health <= 0) return; // already exploded
        // Decrease the fuse each tick and explode once it hits zero
        fuse--;
        if (fuse <= 0) {
            explode(board);
        }
    }

    /**
     * Deals damage to all zombies in a 3x3 area centered on this cherrybomb
     * then removes the plant from the board.
     *
     * @param board the game board
     */
    private void explode(Tile[][] board) {
        int plantRow = this.position.getRow();
        int plantCol = this.position.getColumn();
        // Scan the surrounding 3x3 area for zombies to damage
        for (int i = plantRow - range; i <= plantRow + range; i++) {
            for (int j = plantCol - range; j <= plantCol + range; j++) {
                if (i >= 0 && i < board.length && j >= 0 && j < board[0].length) {
                    Tile tile = board[i][j];
                    if (tile.hasZombies()) {
                        // Copy list to avoid concurrent modification while removing
                        List<Zombie> zs = new ArrayList<>(tile.getZombies());
                        for (Zombie z : zs) {
                            z.takeDamage(this.damage);
                            if (!z.isAlive()) {
                                tile.removeZombie(z);
                            }
                        }
                    }
                }
            }
        }
        System.out.println("Cherrybomb exploded at Row " + plantRow + " Column " + plantCol + "!");
        this.health = 0;
        position.removePlants();
    }

}
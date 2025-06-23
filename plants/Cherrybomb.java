package plants;
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
     * Overloaded to affect zombies in a 3x3 grid around the Plant position
     * @param zombie A 2D array representing a grid of zombies in that position
     * @return 1 To indicate action was done successfuly
     */
    public int action(java.util.List<Zombie>[][] zombies) {
        int plantRow = this.position.getRow();
        int plantCol = this.position.getColumn();
        int rows = zombies.length;               // bounds for row index
        int columns = zombies[0].length;         // bounds for column index

        for (int i = plantRow - this.range; i <= plantRow + this.range; i++) {
            for (int j = plantCol - this.range; j <= plantCol + this.range; j++) {
                if (i >= 0 && i < rows && j >= 0 && j < columns) {
                    java.util.List<Zombie> zs = zombies[i][j];
                    if (zs != null && !zs.isEmpty()) {
                        // copy to avoid concurrent modification
                        java.util.List<Zombie> copy = new java.util.ArrayList<>(zs);
                        for (Zombie z : copy) {
                            z.takeDamage(this.damage);
                            if (!z.isAlive()) {
                                zs.remove(z);
                            }
                        }
                    }
                }
            }
        }
        System.out.println("Cherrybomb exploded at Row " + (plantRow + 1) + " Column " + (plantCol + 1) + "!");
        this.health = 0; // destroy Cherrybomb after exploding
        return 1;
    }

    /**
     * Updates the fuse each tick and triggers explosion when it reaches zero.
     *
     * @param board the current game board used to locate zombies
     */
    public void tick(Tile[][] board) {
        if (health <= 0) return; // already exploded
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
        for (int i = plantRow - range; i <= plantRow + range; i++) {
            for (int j = plantCol - range; j <= plantCol + range; j++) {
                if (i >= 0 && i < board.length && j >= 0 && j < board[0].length) {
                    Tile tile = board[i][j];
                    if (tile.hasZombies()) {
                        // copy to avoid concurrent modification
                        java.util.List<Zombie> zs = new java.util.ArrayList<>(tile.getZombies());
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
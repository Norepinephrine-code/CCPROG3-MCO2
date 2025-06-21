package zombies;
import tiles.Tile;

/**
 * Standard zombie with average attributes.
 */
public class NormalZombie extends Zombie {
    /**
     * Creates a NormalZombie starting at the specified position.
     *
     * @param pos initial tile position
     */
    public NormalZombie(Tile pos) {
        super(70, 4, 10, pos);
    }

}

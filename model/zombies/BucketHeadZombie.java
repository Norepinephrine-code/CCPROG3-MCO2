package model.zombies;
import model.tiles.Tile;

/**
 * Tough zombie wearing a bucket that greatly increases its durability.
 */
public class BucketHeadZombie extends Zombie {

    /**
     * Creates a bucket head zombie starting at the given position.
     *
     * @param pos starting tile
     */
    public BucketHeadZombie(Tile pos) {
        // No specified health range, described as extremely resistant
        super(110,1,5,pos);
    }

}

package plants;
import tiles.Tile;

/**
 * Basic offensive plant that shoots peas at zombies.
 */
public class Peashooter extends Plant {

    /**
     * Creates a Peashooter at the given tile position.
     *
     * @param position tile where the plant will be placed
     */
    public Peashooter(Tile position){
        super(100, 4.5f, 15, 50, 9, 20, 2.0f, position);
    }
}

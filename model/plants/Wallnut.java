package model.plants;
import model.tiles.Tile;
import model.zombies.Zombie;

/**
 * Defensive plant that simply absorbs damage.
 */
public class Wallnut extends Plant {

    /**
     * Creates a wall-nut with high health.
     *
     * @param pos tile where the wall-nut is planted
     */
    public Wallnut(Tile pos) {
        super(50, 30.f,0,200, 0,0,0,pos);
    }
    @Override
    /**
     * Wall-nut does not perform any attacking action.
     */
    public int action(Zombie zombie) {
        return 0;   // Do nothing 
    }
}

package zombies;
import tiles.Tile;

/**
 * Zombie equipped with a cone that provides additional defence at the cost of
 * speed.
 */
public class ConeheadZombie extends Zombie {
    /**
     * Creates a ConeheadZombie at the specified position.
     *
     * @param pos starting tile on the board
     */
    public ConeheadZombie(Tile pos) {
        super(70, 2, 10, pos);
        this.speed = 2;
        this.baseSpeed = 2;
    }

}

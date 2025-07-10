package plants;
import tiles.Tile;

/**
 * Support plant that periodically generates sun resources.
 */
public class Sunflower extends Plant {

    /**
     * Constructs a Sunflower at the specified tile.
     *
     * @param position tile where the plant will be placed
     */
    public Sunflower(Tile position){
        super(50, 2.5f, 0, 40, 0, 0, 2.5f, position);
    }

    /**
     * Overloaded method to generate sun instead of attacking and reducing Zombie health
     * @param sun Amount of sun in-game
     * @return Updated amount of sun
     */
    public int action(int sun){
        sun+=50;
        System.out.println("Sunflower generated sun.");
        return sun;
    }

}
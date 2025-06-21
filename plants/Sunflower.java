package plants;
import tiles.Tile;

public class Sunflower extends Plant {

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
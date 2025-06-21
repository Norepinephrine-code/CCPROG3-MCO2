package plants;
import tiles.Tile;

public class Sunflower extends Plant {

    public Sunflower(Tile position){
        super(50, 5, 0, 300, 0, 0, 2.5, position);
    }

    @Override
    public int action(int sun){
        sun+=50;
        System.out.println("Sunflower generated sun.");
        return sun;
    }

}
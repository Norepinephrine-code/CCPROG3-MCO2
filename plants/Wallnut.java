package plants;
import tiles.Tile;
import zombies.Zombie;

public class Wallnut extends Plant {

    public Wallnut(Tile pos) {
        super(50, 30.f,0,200, 0,0,0,pos);
    }
    @Override
    public int action(Zombie zombie) {
        return 0;   // Do nothing 
    }
}

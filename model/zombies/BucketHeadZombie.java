package model.zombies;
import model.tiles.Tile;

public class BucketHeadZombie extends Zombie {

    public BucketHeadZombie(Tile pos) {

        // No Specified Health Range of the BucketHead Zombie, it was only mentioned as "Extremely Resistant"
        super(110,1,5,pos);

    }

}

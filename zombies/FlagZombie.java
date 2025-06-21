package zombies;
import tiles.Tile;
public class FlagZombie extends Zombie {
    public FlagZombie(Tile pos) {
        super(70, 3, 10, pos);
        this.speed = 3;
        this.baseSpeed = 3;
    }

}

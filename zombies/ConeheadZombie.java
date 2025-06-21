package zombies;
import tiles.Tile;
public class ConeheadZombie extends Zombie {
    public ConeheadZombie(Tile pos) {
        super(70, 2, 10, pos);
        this.speed = 2;
        this.baseSpeed = 2;
    }

}

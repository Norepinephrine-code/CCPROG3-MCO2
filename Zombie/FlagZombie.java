package MCO1_Files.Zombie;

public class FlagZombie extends Zombie {
    public FlagZombie(Tile pos) {
        super(70, 3, 10, pos);
        this.speed = 3;
        this.baseSpeed = 3;
    }

    @Override
    public void move() {
        if (!isFreeze) {
            // movement logic here
        }
    }

    @Override
    public void attack() {
        // attack logic here
    }
}
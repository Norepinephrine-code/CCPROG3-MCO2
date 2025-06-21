package MCO1_Files.Zombie;

public class ConeheadZombie extends Zombie {
    public ConeheadZombie(Tile pos) {
        super(70, 2, 10, pos);
        this.speed = 2;
        this.baseSpeed = 2;
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

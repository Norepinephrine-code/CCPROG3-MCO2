public class NormalZombie extends Zombie {
    public NormalZombie(Tile pos) {
        super(70, 4, 10, pos);
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

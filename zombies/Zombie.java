package zombies;
import tiles.Tile;
public abstract class Zombie {
    protected int health = 70;
    protected int baseSpeed = 4;
    protected int speed = 4;
    protected int damage = 10;
    protected Tile position;

    protected boolean isFreeze;
    protected int freezeTimer;

    public Zombie(int h, int bs, int d, Tile pos) {
        this.health = h;
        this.baseSpeed = bs;
        this.speed = bs;
        this.damage = d;
        this.position = pos;
        this.isFreeze = false;
        this.freezeTimer = 0;
    }

    public abstract void move();

    public void takeDamage(int amount) {
        health -= amount;
    }

    public void freezeFor(int ticks) {
        isFreeze = true;
        freezeTimer = ticks;
    }

    public void update() {
        if (isFreeze) {
            freezeTimer--;
            if (freezeTimer <= 0) {
                isFreeze = false;
            }
        }
    }

    public abstract void attack();

    public boolean isAlive() {
        return health > 0;
    }

    public Tile getPosition() {
        return position;
    }
}

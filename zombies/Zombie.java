package zombies;
import tiles.Tile;
import plants.Plant;
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

    public void move(Tile[][] board) {
        int row = position.getRow();
        int col = position.getColumn();

        // Calculate effective speed (frozen zombies move slower)
        int moveDistance = isFreeze ? Math.max(1, speed / 2) : speed;
        int newCol = col - moveDistance;

        if (newCol < 0) {
            System.out.println("A zombie reached your house! Game Over.");
            // Optionally set a game-over flag here
            return;
        }

        board[row][col].removeZombie(this);
        board[row][newCol].addZombie(this);
        this.setPosition(board[row][newCol]);

        System.out.println("Zombie moved to Row " + row + ", Col " + newCol +
                (isFreeze ? " (slowed)" : ""));
    }

    public void setPosition(Tile position) {
        this.position = position;
    }

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

    public void attack() {
        Tile currentTile = this.getPosition();
        if (currentTile.getPlant() != null) {
            currentTile.getPlant().takeDamage(this);
            System.out.println("Zombie at Row " + currentTile.getRow() + ", Col " + currentTile.getColumn() +
                    " attacked the plant for " + this.getDamage() + " damage.");
        }
    }

    public boolean isAlive() {
        return health > 0;
    }

    public Tile getPosition() {
        return position;
    }

    public int getBaseSpeed() {
        return baseSpeed;
    }

    public int getSpeed() {
        return speed;
    }

    public int getHealth() {
        return health;
    }
    
    public int getDamage() {
        return damage;
    }

    public int getFreezeTimer() {
        return freezeTimer;
    }

    public
}

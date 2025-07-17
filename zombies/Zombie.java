package zombies;
import plants.Plant;
import tiles.Tile;

/**
 * Base class for all zombie types. Implements common behaviour such as
 * movement, attacking and taking damage.
 */
public abstract class Zombie {
    
    // Property Variables //
    protected int health;
    protected int baseSpeed;
    protected int speed;
    protected int damage;
    protected Tile position;

    // State Variables //
    protected boolean isFreeze = false;
    protected int freezeTimer = 0;

    /**
     * Constructs a zombie with the given attributes.
     *
     * @param h   starting health
     * @param bs  base movement speed in ticks
     * @param d   damage dealt to plants
     * @param pos starting tile position on the board
     */
    public Zombie(int h, int bs, int d, Tile pos) {
        this.health = h;
        this.baseSpeed = bs;
        this.speed = bs;
        this.damage = d;
        this.position = pos;

        this.isFreeze = false;
        this.freezeTimer = 0;
    }

    /**
     * Moves the zombie one tile to the left on the given board.
     *
     * @param board current board grid used to update tile occupancy
     */
    public void move(Tile[][] board) {
        // remove from current tile
        Tile currentTile = this.getPosition();
        currentTile.removeZombie(this);

        // move 1 tile left ONLY:
        int newCol = this.getPosition().getColumn() - 1;
        if (newCol < 0) newCol = 0; // safeguard

        Tile newTile = board[this.getPosition().getRow()][newCol];
        this.setPosition(newTile);
        newTile.addZombie(this);
    }

    /**
     * Updates the tile reference that this zombie occupies.
     *
     * @param position new tile position
     */
    public void setPosition(Tile position) {
        this.position = position;
    }

    /**
     * Reduces this zombie's health by the specified amount.
     *
     * @param amount damage to apply
     */
    public void takeDamage(int amount) {
        health -= amount;
    }

    /**
     * Freezes the zombie for the specified number of ticks.
     *
     * @param ticks how long the zombie will remain frozen
     */
    public void freezeFor(int ticks) {
        isFreeze = true;
        freezeTimer = ticks;
    }

    /**
     * Updates internal timers such as freeze state each tick.
     */
    public void update() {
        if (isFreeze) {
            freezeTimer--;
            if (freezeTimer <= 0) {
                isFreeze = false;
            }
        }
    }

    /**
     * Attacks the plant on the current tile if one exists.
     * The Tile[][] board is never used here. It is only used by the PoleVaultingZombie for jump purposes
     * during its first attack
     */
    public void attack(Tile[][] board) {
        Tile currentTile = this.getPosition();
        Plant target = currentTile.getPlant();
        if (target != null) {
            target.takeDamage(this);
            System.out.println("Zombie at Row " + (currentTile.getRow() + 1) + ", Col " +
                    (currentTile.getColumn() + 1) + " attacked the plant for " + this.getDamage() +
                    " damage.");
            if (!target.isAlive()) {
                currentTile.removePlant();
                System.out.println("Plant at Row " + (currentTile.getRow() + 1) + ", Col " +
                        (currentTile.getColumn() + 1) + " was destroyed.");
            }
        }
    }

    /**
     * Checks if this zombie still has health remaining.
     *
     * @return {@code true} if health is above zero
     */
    public boolean isAlive() {
        return health > 0;
    }

    /**
     * @return current tile occupied by the zombie
     */
    public Tile getPosition() {
        return position;
    }

    /**
     * @return zombie's original speed value
     */
    public int getBaseSpeed() {
        return baseSpeed;
    }

    /**
     * @return current movement speed factoring in modifiers
     */
    public int getSpeed() {

        return isFreeze ? (this.speed * 2) : (this.baseSpeed);
    }

    /**
     * @return current health value
     */
    public int getHealth() {
        return health;
    }
    
    /**
     * @return damage dealt when attacking a plant
     */
    public int getDamage() {
        return damage;
    }

    /**
     * @return remaining ticks that this zombie is frozen
     */
    public int getFreezeTimer() {
        return freezeTimer;
    }
}

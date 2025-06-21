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

    public abstract void attack();

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

    public boolean isAlive() {
        return health > 0;
    }

    // Getters
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

    public boolean isFrozen() {
        return isFreeze;
    }

    public int getFreezeTimer() {
        return freezeTimer;
    }

    // Setters
    public void setHealth(int health) {
        this.health = health;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setPosition(Tile position) {
        this.position = position;
    }

    public void setFrozen(boolean frozen) {
        this.isFreeze = frozen;
    }

    public void setFreezeTimer(int freezeTimer) {
        this.freezeTimer = freezeTimer;
    }
}

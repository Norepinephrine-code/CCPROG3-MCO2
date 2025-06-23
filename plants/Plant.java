/**
 * The Plant class represents the Plants derived from "Plants vs Zombies".
 */

package plants;
import tiles.Tile;
import zombies.Zombie;

public class Plant{

    // Constructor

    /**
     * Constructs a plant with all given attributes
     * @param cost Amount of sun needed to use the plant
     * @param regenerateRate Amount of time the plant needs to regenerate
     * @param damage Amount of damage the plant does to the zombie
     * @param health Amount of damage the plant can sustain
     * @param range How far the attack of the plant can reach
     * @param directDamage Amount of damage the plant does to zombies at a closer range
     * @param speed How fast the next attack will be
     * @param position The position of the plant
     */
    public Plant(int cost, float regenerateRate, int damage, int health, int range, int directDamage, float speed, Tile position){
        this.cost = cost;
        this.regenerateRate = regenerateRate;
        this.damage = damage;
        this.health = health;
        this.range = range;
        this.directDamage = directDamage;
        this.speed = speed;
        this.position = position;
    }

    // Methods

    /**
     * The plant attacks zombies in their row
     * @param Zombie The zombie being attacked
     * @return The health of the zombie after getting hit by the action
     */
    public int action(Zombie zombie) {
        // Determine relative positions for the plant and its target zombie
        Tile zombieTile = zombie.getPosition();
        int plantRow = this.position.getRow();
        int plantCol = this.position.getColumn();
        int zombieRow = zombieTile.getRow();
        int zombieCol = zombieTile.getColumn();

        // Pre-computed values used only for printing logs
        int plantRowDisplay = plantRow + 1;
        int plantColDisplay = plantCol + 1;
        int zombieRowDisplay = zombieRow + 1;
        int zombieColDisplay = zombieCol + 1;

        // Ignore if zombie is on a different row
        if (plantRow != zombieRow) return zombie.getHealth();

        int distance = zombieCol - plantCol;

        // Check if the zombie is within attack range
        if (distance > 0 && distance <= range) {
            if (distance <= range / 2) {
                // Close enough for direct damage
                zombie.takeDamage(directDamage);
                System.out.println("Plant at Row " + plantRowDisplay + ", Column " + plantColDisplay +
                        " close-range hit zombie at Row " + zombieRowDisplay + ", Column " +
                        zombieColDisplay + " for " + directDamage + " damage.");
            } else {
                // Farther away: reduce damage based on distance
                int dmg = (damage - (distance / 2));
                zombie.takeDamage(dmg);
                System.out.println("Plant at Row " + plantRowDisplay + ", Column " + plantColDisplay +
                        " long-range hit zombie at Row " + zombieRowDisplay + ", Column " +
                        zombieColDisplay + " for " + dmg + " damage.");
            }
        }

        return zombie.getHealth();
    }
        

    /**
     * The plant takes damage to its health according to the damage of the zombie
     * @param zombie The zombie attacking the plant
     */
    public void takeDamage(Zombie zombie){
        this.health -= zombie.getDamage();
    }

    /**
     * @return Boolean based on Plant health
     */
    public boolean isAlive(){
        return health > 0;
    }

    // Getters

    /**
     * @return The current health of the plant
     */
    public float getHealth(){
        return this.health;
    }

    /**
     * @return The damage the plant deals
     */
    public float getDamage(){
        return this.damage;
    }

    /**
     * @return The damage plant deals at a closer range
     */
    public float getDirectDamage(){
        return this.directDamage;
    }

    /**
     * @return The range of the plant's attack
     */
    public float getRange(){
        return this.range;
    }

    /**
     * @return The speed of the plant's next attack
     */
    public float getSpeed(){
        return this.speed;
    }

    /**
     * @return How much the plant costs to place
     */
    public int getCost(){
        return this.cost;
    }

    /**
     * @return How fast plants can be generated
     */
    public float getRegenerateRate(){
        return this.regenerateRate;
    }

    /**
     * @return Position of the plant
     */
    public Tile getPosition(){
        return this.position;
    }

    // Attributes

    /* Amount of sun needed to use the plant */
    protected int cost;

    /* Amount of time the plant needs to regenerate */
    protected float regenerateRate;

    /* Amount of damage the plant does to the zombie */
    protected int damage;

    /* Amount of damage the plant can sustain */
    protected int health;

    /* How far the attack of the plant can reach */
    protected int range;

    /* Amount of damage the plant does to zombies at a closer range */
    protected int directDamage;

    /* How fast the next attack will be */
    protected float speed;    

    /* The position of the plant */
    protected Tile position;
}

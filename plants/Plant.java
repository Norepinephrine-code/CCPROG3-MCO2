/**
 * The Plant class represents the Plants derived from "Plants vs Zombies".
 */

package plants;
import tiles.Tile;
import zombies.Zombie;

public class Plant{

    public Plant(int cost, float regenerateRate, float damage, float health, int range, float directDamage, float speed, Tile position){
        cost = this.cost;
        regenerateRate = this.regenerateRate;
        damage = this.damage;
        health = this.health;
        range = this.range;
        directDamage = this.directDamage;
        speed = this.speed;
        position = this.position;
    }

    /**
     * The plant attacks zombies in their row
     * @param Zombie The zombie being attacked
     * @return The health of the zombie after getting hit by the action
     */
    public int action(Zombie zombie) {
        Tile zombieTile = zombie.getPosition();
        int plantRow = this.position.getRow();
        int plantCol = this.position.getColumn();
        int zombieRow = zombieTile.getRow();
        int zombieCol = zombieTile.getColumn();
    
        // Only attack if in the same row and ahead
        if (plantRow != zombieRow) return zombie.getHealth();
    
        int distance = zombieCol - plantCol;
    
        if (distance > 0 && distance <= range) {
            if (distance <= range / 2) {
                zombie.takeDamage((int) directDamage);
                System.out.println("Close-range hit! Dealt " + (int) directDamage + " damage.");
            } else {
                int dmg = (int)(damage / (distance / 2.0));
                zombie.takeDamage(dmg);
                System.out.println("Long-range hit! Dealt " + dmg + " damage.");
            }
        }
    
        return zombie.getHealth();
    }
        

    /**
     * The plant takes damage to its health according to the damage of the zombie
     * @param zombie The zombie attacking the plant
     */
    public void takeDamage(Zombie zombie){
        this.health = this.health - zombie.damage
    }

    public int getCost(){}

    
    /* Amount of sun needed to use the plant */
    protected int cost;

    /* Amount of time the plant needs to regenerate */
    protected float regenerateRate;

    /* Amount of damage the plant does to the zombie */
    protected float damage;

    /* Amount of damage the plant can sustain */
    protected float health;

    /* How far the attack of the plant can reach */
    protected int range;

    /* Amount of damage the plant does to zombies at a closer range */
    protected float directDamage;

    /* How fast the next attack will be */
    protected float speed;    

    /* The position of the plant */
    protected Tile position;
}

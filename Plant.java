/**
 * The Plant class represents the Plants derived from "Plants vs Zombies".
 */

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
    public int action(Zombie zombie){
        int i;

                for (i=0; i<this.range; i++){
                    if (i > (this.range/2) && i == zombie.position){
                        zombie.health = zombie.health - (this.damage/(i/2));
                        System.out.println("Hit zombie at row " + zombie.position.row + " column " + zombie.position.column);
                        return zombie.health; // return value to simulate a projectile hitting only the first zombie
                        }
                    else if (i < (this.range/2 && i = zombiePosition)){
                        zombie.health = zombie.health - this.directDamage;
                        return zombie.health; // return value to simulate a projectile hitting only the first zombie
                        }
                    
                }
        
     }
    

    /**
     * The plant takes damage to its health according to the damage of the zombie
     * @param zombie The zombie attacking the plant
     */
    public void takeDamage(Zombie zombie){
        this.health = this.health - zombie.damage
    }

    public int getCost(){}


    public class Sunflower extends Plant{
        
        Public Sunflower(Tile position){
        super(50, 5, 0, 300, 0, 0, 2.5, position);
        }

        @Override
        public int action(int sun){
            sun+=50;
            System.out.println("Sunflower generated sun.");
            return sun;
        }

    }

    public class Peashooter extends Plant{
        
        Public Peashooter(Tile position){
        super(100, 2.5, 15, 300, 9, 20, 2, position);
        }
    }

    public class Cherrybomb extends Plant{

        Public Cherrybomb(Tile position){
        super(150,15,1800,1000,1,1800,15,position);
        }

        @Override
        public int action(Zombie[][] zombie){
            int i, j;
            for (i=this.position.row-this.range;this.position.row+range;i++){
                for (j=this.position.column-this.range;j<this.position.column+range;j++){
                    if (zombie[i][j].position.row == i && zombie[i][j].position.column == j){
                        zombie.health[i][j] = zombie.health[i][j] - this.damage;
                    }

                }
            }
            this.health = 0; // destroy Cherrybomb after exploding
        }

    }
    
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
package model.plants;

import java.util.ArrayList;

import model.tiles.Tile;
import model.zombies.Zombie;

/**
 * Delayed explosive mine that arms after a short time and detonates on contact
 * with a zombie.
 */
public class PotatoMine extends Plant {

    private int armTime;
    private final int ARM_THRESHOLD = 5;
    boolean hasArmed = false;


    /**
     * Creates a potato mine at the specified tile.
     *
     * @param position tile where the mine is planted
     */
    public PotatoMine(Tile position){
        super(150,5.5f,1800,1000,1,1800,5.5f,position);
        this.armTime = 0;
    }


    /**
     * Executes the mine's behaviour each tick.
     */
    public void action(Tile[][] board) {
        armExplode();
    }
    
    // void action() is not used because we want to make it more clearer with what it does by writing a new
    // method instead called armExplode();

    /**
     * Arms the mine after a delay and explodes when a zombie steps on it.
     */
    public void armExplode() {
        if (!hasArmed) {
            armTime++;
            hasArmed = (armTime >= ARM_THRESHOLD);
        }

        if (hasArmed && this.position.hasZombies()) {
            for (Zombie z : new ArrayList<>(this.position.getZombies())) {
                z.takeDamage(1000);
                if (!z.isAlive()) {
                    listener.onZombieKilled(z);
                }
            }

        
            listener.onPlantKilled(this); // The mine explodes and dies
        
        }
    }

}
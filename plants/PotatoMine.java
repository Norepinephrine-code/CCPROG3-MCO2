package plants;

import java.util.ArrayList;
import tiles.Tile;
import zombies.Zombie;


public class PotatoMine extends Plant {

    private int armTime;
    private final int ARM_THRESHOLD = 5;
    boolean hasArmed = false;


    public PotatoMine(Tile position){
        super(150,5.5f,1800,1000,1,1800,5.5f,position);
        this.armTime = 0;
    }


    public void action(Tile[][] board) {
        armExplode();
    }
    
    // void action() is not used because we want to make it more clearer with what it does by writing a new
    // method instead called armExplode();

    public void armExplode() {
        if (!hasArmed) {
            armTime++;
            hasArmed = (armTime >= ARM_THRESHOLD);
        }

        if (hasArmed && this.position.hasZombies()) {
            for (Zombie z : new ArrayList<>(this.position.getZombies())) {
                z.takeDamage(1000);
                if (!z.isAlive() && listener != null) {
                    listener.onZombieKilled(z);
                }
            }

            if (listener != null) {
                listener.onPlantKilled(this); // The mine explodes and dies
            }
        }
    }

}
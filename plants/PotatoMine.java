package plants;

import java.util.ArrayList;
import java.util.List;

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

    public void armExplode() {
        armTime += (hasArmed)? 0:1;                   // Armed? Add nothing, else increment count by 1.
        hasArmed = (armTime >= ARM_THRESHOLD);        // hasArmed? Returns True if it has already reached to THRESHOLD.

        if (this.position.hasZombies() && hasArmed) {
            List <Zombie> zombies = this.position.getZombies();
            for (Zombie z:zombies) {
                z.takeDamage(1000);             // This can be any number, but just Instantly kill it.
            }

            //******************DEATH******************//
            this.health = 0;
            this.getPosition().removePlant();
            
        }
    }

}
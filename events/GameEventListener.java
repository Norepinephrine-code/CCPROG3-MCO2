package events;

import plants.Plant;
import zombies.Zombie;

/**
 * Listener interface for game events such as entity deaths.
 */
public interface GameEventListener {
    /** Called when a zombie dies. */
    void onZombieKilled(Zombie z);

    /** Called when a plant dies or is removed. */
    void onPlantKilled(Plant p);
}

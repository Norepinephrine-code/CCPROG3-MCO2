package events;

import model.plants.*;
import model.tiles.Tile;
import model.zombies.Zombie;

/**
 * Listener interface for game events such as entity deaths, creation, and sun generation.
 */
public interface GameEventListener {
    /** Called when a zombie dies. */
    void onZombieKilled(Zombie z);

    /** Called when a plant dies or is removed. */
    void onPlantKilled(Plant p);

    void onPlantRemoved(Plant p);

    void onZombieMove(Tile newTile, Tile currentTile, Zombie z);

    void onZombieGenerated(Zombie z);

    void onSetPlant(Plant p);

    void onCollectSun(Sunflower sf);

    void onReadySun(Sunflower sf);
}

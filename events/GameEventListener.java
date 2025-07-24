package events;

import model.plants.*;
import model.tiles.Tile;
import model.zombies.Zombie;

/**
 * Listener interface for game events such as entity deaths, creation, and sun generation.
 */
public interface GameEventListener {
    /**
     * Called when a zombie is killed and removed from the board.
     *
     * @param z zombie that died
     */
    void onZombieKilled(Zombie z);

    /**
     * Called when a plant dies due to damage or effects.
     *
     * @param p plant that was killed
     */
    void onPlantKilled(Plant p);

    /**
     * Called when a plant is manually removed by the player.
     *
     * @param p plant that was removed
     */
    void onPlantRemoved(Plant p);

    /**
     * Fired whenever a zombie moves to a new tile.
     *
     * @param newTile     tile the zombie moved to
     * @param currentTile previous tile of the zombie
     * @param z           zombie that moved
     */
    void onZombieMove(Tile newTile, Tile currentTile, Zombie z);

    /**
     * Called when a zombie is created/spawned.
     *
     * @param z new zombie instance
     */
    void onZombieGenerated(Zombie z);

    /**
     * Invoked after a plant is successfully placed on the board.
     *
     * @param p newly placed plant
     */
    void onSetPlant(Plant p);

    /**
     * Player collects sun directly from a sunflower.
     *
     * @param sf sunflower that generated the sun
     */
    void onCollectSunFromSunflower(Sunflower sf);

    /**
     * Player collects a dropped sun from a tile.
     *
     * @param tile tile from which sun was collected
     */
    void onCollectSunFromTile(Tile tile);

    /**
     * Sun has dropped on the specified tile.
     *
     * @param tile tile receiving a sun drop
     */
    void onDroppedSun(Tile tile);

    /**
     * Sunflower is ready to be harvested by the player.
     *
     * @param sf sunflower containing ready sun
     */
    void onReadySun(Sunflower sf);
}

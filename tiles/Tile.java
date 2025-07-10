package tiles;

import java.util.ArrayList;
import java.util.List;
import plants.Plant;
import zombies.Zombie;

/**
 * Represents a single grid square on the board. A tile may hold a plant
 * and multiple zombies at any given time.
 */
public class Tile {
    private int row;
    private int column;
    private Plant plant;
    private List<Zombie> zombies;

    /**
     * Creates a tile located at the given row and column.
     *
     * @param row    row index of the tile
     * @param column column index of the tile
     */
    public Tile(int row, int column) {
        this.row = row;
        this.column = column;
        this.zombies = new ArrayList<>();
    }

    /**
     * Places a plant on this tile.
     *
     * @param plant plant instance to place
     */
    public void setPlant(Plant plant) {
        this.plant = plant;
    }

    /**
     * Clears any plant currently occupying this tile.
     */
    public void removePlant() {
        this.plant = null;
    }

    /**
     * @return {@code true} if a plant is currently placed on this tile
     */
    public boolean isOccupied() {
        return plant != null;
    }

    /**
     * Adds a zombie to this tile's zombie list.
     *
     * @param z zombie entering the tile
     */
    public void addZombie(Zombie z) {
        zombies.add(z);
    }

    /**
     * Removes the specified zombie from this tile.
     *
     * @param z zombie to remove
     */
    public void removeZombie(Zombie z) {
        zombies.remove(z);
    }

    /**
     * @return row index of the tile
     */
    public int getRow() {
        return row;
    }

    /**
     * @return column index of the tile
     */
    public int getColumn() {
        return column;
    }

    /**
     * @return plant occupying the tile, or {@code null}
     */
    public Plant getPlant() {
        return plant;
    }

    /**
     * @return {@code true} if any zombies are present on this tile
     */
    public boolean hasZombies() {
        return !zombies.isEmpty();
    }
    
    /**
     * @return list of zombies currently on this tile
     */
    public List<Zombie> getZombies() {
        return zombies;
    }
}

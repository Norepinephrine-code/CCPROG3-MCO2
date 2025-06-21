package tiles;

import java.util.ArrayList;
import java.util.List;
import zombies.Zombie;
import plants.Plant;

public class Tile {
    private int row;
    private int column;
    private Plant plants;
    private List<Zombie> zombies;

    public Tile(int row, int column) {
        this.row = row;
        this.column = column;
        this.zombies = new ArrayList<>();
    }

    public void setPlant(Plant plant) {
        this.plants = plant;
    }

    public void removePlants() {
        this.plants = null;
    }

    public boolean isOccupied() {
        return plants != null;
    }

    public void addZombie(Zombie z) {
        zombies.add(z);
    }

    public void removeZombie(Zombie z) {
        zombies.remove(z);
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public Plant getPlants() {
        return plants;
    }

    public List<Zombie> getZombies() {
        return zombies;
    }
}

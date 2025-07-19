package model.plants;
import model.tiles.Tile;

/**
 * Support plant that periodically generates sun resources.
 */
public class Sunflower extends Plant {

    private final int DURATION = 5;
    private int tick_timer;
    private boolean hasSun;

    /**
     * Constructs a Sunflower at the specified tile.
     *
     * @param position tile where the plant will be placed
     */
    public Sunflower(Tile position){
        super(50, 2.5f, 0, 40, 0, 0, 2.5f, position);
        this.tick_timer = 0;
        this.hasSun = false;
    }

    /**
     * Overloaded method to generate sun instead of attacking and reducing Zombie health
     * @param sun Amount of sun in-game
     * @return Updated amount of sun
     */
    public void action(){
        if (tick_timer < DURATION) tick_timer++;

        if (tick_timer >= DURATION && hasSun==false) {
            hasSun=true; 
            listener.onReadySun(this);
        }
    }

    public void collect() {
        if (hasSun==true) {
            hasSun = false;                  // Reset Collection
            tick_timer = 0;                  // Reset Timer
            listener.onCollectSun(this);     // Inform Game to Generate Sun
        }
    }

    public boolean hasSun() {
        return this.hasSun;
    }

}
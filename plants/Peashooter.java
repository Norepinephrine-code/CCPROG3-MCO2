package plants;
import tiles.Tile;

/**
 * Basic offensive plant that shoots peas at zombies.
 */
public class Peashooter extends Plant {

    // === BEGIN cooldown fields ===
    /** Number of ticks this peashooter must wait before firing again. */
    private static final int COOLDOWN_TICKS = 1; // attack every other tick

    /** Remaining ticks until the next allowed attack. */
    private int cooldownCounter = 0;
    // === END cooldown fields ===

    /**
     * Creates a Peashooter at the given tile position.
     *
     * @param position tile where the plant will be placed
     */
    public Peashooter(Tile position){
        super(100, 4.5f, 15, 50, 9, 20, 2.0f, position);
    }

    /**
     * Overrides Plant.action to include a simple cooldown.
     * Peashooter only fires when {@code cooldownCounter} reaches zero,
     * then resets the counter to {@link #COOLDOWN_TICKS}.
     */
    @Override
    public int action(zombies.Zombie zombie) {
        // === BEGIN Peashooter cooldown logic ===
        // Skip attack if still cooling down.
        if (cooldownCounter > 0) {
            cooldownCounter--;
            return zombie.getHealth();
        }

        int before = zombie.getHealth();
        int after = super.action(zombie);
        // If damage was dealt, start cooldown.
        if (after != before) {
            cooldownCounter = COOLDOWN_TICKS;
        }
        // === END Peashooter cooldown logic ===
        return after;
    }
}

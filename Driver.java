/**
 * Entry point for the console-based Plants vs Zombies game.
 * This class now simply launches the {@link Game} which
 * contains all gameplay logic.
 */
public class Driver {
    /**
     * Launches the game using default settings.
     *
     * @param args command line arguments (unused)
     */
    public static void main(String[] args) {
        new Game().start();
    }
}

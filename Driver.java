import javax.swing.SwingUtilities;
import view.LevelSelectorGUI;

/*
 * 
  ░██████     ░███    ░███     ░███ ░██████████       ░███████   ░█████████  ░██████░██    ░██ ░██████████ ░█████████  
 ░██   ░██   ░██░██   ░████   ░████ ░██               ░██   ░██  ░██     ░██   ░██  ░██    ░██ ░██         ░██     ░██ 
░██         ░██  ░██  ░██░██ ░██░██ ░██               ░██    ░██ ░██     ░██   ░██  ░██    ░██ ░██         ░██     ░██ 
░██  █████ ░█████████ ░██ ░████ ░██ ░█████████        ░██    ░██ ░█████████    ░██  ░██    ░██ ░█████████  ░█████████  
░██     ██ ░██    ░██ ░██  ░██  ░██ ░██               ░██    ░██ ░██   ░██     ░██   ░██  ░██  ░██         ░██   ░██   
 ░██  ░███ ░██    ░██ ░██       ░██ ░██               ░██   ░██  ░██    ░██    ░██    ░██░██   ░██         ░██    ░██  
  ░█████░█ ░██    ░██ ░██       ░██ ░██████████       ░███████   ░██     ░██ ░██████   ░███    ░██████████ ░██     ░██ 
     
    - Main entry point of the game.
 */

/**
 * Entry point that starts the level selector GUI.
 */
public class Driver {
    /**
     * Launches the game using default settings.
     *
     * @param args command line arguments (unused)
     */
    public static void main(String[] args) {
         SwingUtilities.invokeLater(() -> new LevelSelectorGUI());
    }
}

import java.awt.*;
import javax.swing.*;

public class LevelSelectorGUI extends JFrame {
    public LevelSelectorGUI() {
        setTitle("Select Your Lawn");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(300, 200);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 1, 10, 30));

        JButton sunnyBtn = new JButton("[LEVEL 1] Sunny Lawn");
        JButton graveyardBtn = new JButton("[LEVEL 2] Graveyard Lawn");
        JButton icyBtn = new JButton("[LEVEL 3] Icy Lawn");

        sunnyBtn.addActionListener(e -> launchLevel(1));
        graveyardBtn.addActionListener(e -> launchLevel(2));
        icyBtn.addActionListener(e -> launchLevel(3));

        add(sunnyBtn);
        add(graveyardBtn);
        add(icyBtn);

        setVisible(true);
    }

    private void launchLevel(int level) {
        dispose(); // close selector window
        new Game(level).start(); // launch game with selected level
    }
}

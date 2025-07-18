import java.awt.*;
import javax.swing.*;

public class LevelSelectorGUI extends JFrame {

    public LevelSelectorGUI() {
        setTitle("Select Your Lawn");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 300);
        setResizable(false);
        setLocationRelativeTo(null);

        // Outer panel with vertical BoxLayout
        JPanel outerPanel = new JPanel();
        outerPanel.setLayout(new BoxLayout(outerPanel, BoxLayout.Y_AXIS));
        outerPanel.setBackground(Color.BLACK);
        outerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Title
        JLabel titleLabel = new JLabel("Plants vs Zombies");
        titleLabel.setFont(new Font("Verdana", Font.BOLD, 26));
        titleLabel.setForeground(Color.GREEN);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Subtitle
        JLabel subLabel = new JLabel("by Ramos & Vergara");
        subLabel.setFont(new Font("Verdana", Font.PLAIN, 12));
        subLabel.setForeground(Color.WHITE);
        subLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add title and subtitle with spacing
        outerPanel.add(titleLabel);
        outerPanel.add(Box.createVerticalStrut(5));
        outerPanel.add(subLabel);
        outerPanel.add(Box.createVerticalStrut(30)); // Space before buttons


        // Creating a panel of 3 rows
        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 20));
        buttonPanel.setBackground(Color.BLACK); 

        // Button Creation and initialization
        Color darkGreen = new Color(0, 100, 0); // Dark green
        JButton sunnyBtn = createStyledButton("[LEVEL 1]", darkGreen);
        JButton graveyardBtn = createStyledButton("[LEVEL 2]", darkGreen);
        JButton icyBtn = createStyledButton("[LEVEL 3]", darkGreen);

        sunnyBtn.addActionListener(e -> launchLevel(1));
        graveyardBtn.addActionListener(e -> launchLevel(2));
        icyBtn.addActionListener(e -> launchLevel(3));

        
        // Adding buttons into the Panel
        buttonPanel.add(sunnyBtn);
        buttonPanel.add(graveyardBtn);
        buttonPanel.add(icyBtn);

        outerPanel.add(buttonPanel, BorderLayout.CENTER);
        setContentPane(outerPanel);

        setVisible(true);
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 16));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE); // White text
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(200, 40));
        return btn;
    }

    private void launchLevel(int level) {
        dispose(); // close selector window
        Game game = new Game(level);
        new Thread(() -> game.start()).start(); // run game on its own thread
    }
}

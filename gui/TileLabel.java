package gui;

import java.awt.*;
import javax.swing.*;

public class TileLabel extends JLabel {
    private Icon backgroundIcon;
    private Icon houseIcon;
    private Icon plantIcon;
    private Icon zombieIcon;

    public TileLabel() {
        setHorizontalAlignment(CENTER);
        setVerticalAlignment(CENTER);
        setOpaque(false);
    }

    public void setIcons(Icon background, Icon house, Icon plant, Icon zombie) {
        this.backgroundIcon = background;
        this.houseIcon = house;
        this.plantIcon = plant;
        this.zombieIcon = zombie;
        repaint();
    }

    @Override

    protected void paintComponent(Graphics g) {     
        super.paintComponent(g);
        if (backgroundIcon != null) {
            backgroundIcon.paintIcon(this, g, 0, 0);
            // System.out.println("Background Icon Components are placed!");
        }
        if (houseIcon != null) {
            houseIcon.paintIcon(this, g, 0, 0);
            // System.out.println("House Icon Components are placed!");
        }
        if (plantIcon != null) {
            plantIcon.paintIcon(this, g, 0, 0);
            // System.out.println("Plant Icon Components are placed!");
        }
            
        if (zombieIcon != null) {
            zombieIcon.paintIcon(this, g, 0, 0);
            // System.out.println("Zombie Icon Components are placed!");
        }
    }
}

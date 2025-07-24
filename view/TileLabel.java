package view;

import java.awt.*;
import javax.swing.*;

/**
 * Custom {@link JLabel} that can display layered icons representing the
 * background, house, plant and zombie for a single board tile.
 */
public class TileLabel extends JLabel {
    private Icon backgroundIcon;
    private Icon houseIcon;
    private Icon plantIcon;
    private Icon zombieIcon;

    /**
     * Creates an empty tile label with centered alignment.
     */
    public TileLabel() {
        setHorizontalAlignment(CENTER);
        setVerticalAlignment(CENTER);
        setOpaque(false);
    }

    /**
     * Sets the icons to be painted on this label.
     *
     * @param background background tile image
     * @param house      house/lawn mower image
     * @param plant      plant image for this tile
     * @param zombie     zombie image for this tile
     */
    public void setIcons(Icon background, Icon house, Icon plant, Icon zombie) {
        this.backgroundIcon = background;
        this.houseIcon = house;
        this.plantIcon = plant;
        this.zombieIcon = zombie;
        repaint();
    }

    /**
     * Paints the configured icons in order onto this label.
     */
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

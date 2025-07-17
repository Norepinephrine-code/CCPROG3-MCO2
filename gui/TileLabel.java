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
        if (backgroundIcon != null)                             // LAYERING HAPPENS HERE, order matters!!!
            backgroundIcon.paintIcon(this, g, 0, 0);
        if (houseIcon != null)
            houseIcon.paintIcon(this, g, 0, 0);
        if (plantIcon != null)
            plantIcon.paintIcon(this, g, 0, 0);
        if (zombieIcon != null)
            zombieIcon.paintIcon(this, g, 0, 0);
        super.paintComponent(g);
    }
}

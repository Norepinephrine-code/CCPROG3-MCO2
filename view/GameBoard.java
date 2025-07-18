package view;


import model.plants.Cherrybomb;
import model.plants.FreezePeashooter;
import model.plants.Peashooter;
import model.plants.Plant;
import model.plants.PotatoMine;
import model.plants.Sunflower;
import model.plants.Wallnut;
import model.tiles.Tile;
import model.zombies.BucketHeadZombie;
import model.zombies.ConeheadZombie;
import model.zombies.FlagZombie;
import model.zombies.NormalZombie;
import model.zombies.PoleVaultingZombie;
import model.zombies.Zombie;

/**
 * Utility class responsible for rendering the state of the board
 * in the console. It does not contain any game logic.
 */
public class GameBoard {

    private Tile[][] board;
    private int rows;
    private int cols;

    /**
     * Creates a wrapper around the given tile grid.
     *
     * @param board two dimensional array representing the play field
     */
    public GameBoard(Tile[][] board) {
        this.board = board;
        this.rows = board.length;
        this.cols = board[0].length;
    }

    /**
     * Prints a textual representation of the board using ASCII characters.
     * <p>
     * "---" represents an empty tile, "S" is a {@link Sunflower},
     * "P" a {@link Peashooter}. Zombie tiles show one of:
     * "ZN" for {@link NormalZombie}, "ZF" for {@link FlagZombie},
     * or "ZC" for {@link ConeheadZombie}. Multiple zombies show " M ".
     * Column 0 always shows " H " to indicate the player's house.
     */
    public void display() {
        System.out.println("\n======= Current Game Board  =======");
        // Loop over every board coordinate row by row
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                String content;

                if (c == 0) {
                    // Column 0 is always the player's house
                    content = " H ";
                } else {
                    Tile tile = board[r][c];
                    Plant p = tile.getPlant();
                    boolean hasZombie = tile.hasZombies();
                    boolean hasPlant = tile.isOccupied();

                    // Decide what symbol to print for this tile
                    if (hasZombie && hasPlant) {
                        content = "!X!";

                    } else if (hasZombie) {
                        // Multiple zombies shown with 'M'
                        if (tile.getZombies().size() > 1) {
                            content = " M ";
                        } else {
                            // Otherwise map the single zombie type
                            Zombie z = tile.getZombies().get(0);
                            if (z instanceof FlagZombie) {
                                content = "ZF ";
                            } else if (z instanceof ConeheadZombie) {
                                content = "ZC ";
                            } else if (z instanceof NormalZombie) {
                                content = "ZN ";
                            } else if (z instanceof PoleVaultingZombie) {
                                content = "ZPV";
                             } else if (z instanceof BucketHeadZombie) {
                                content = "ZBH";
                             } else {
                                content = "Z??";
                             }
                        }

                    } else if (hasPlant) {
                                  
                            if (p instanceof Sunflower) {
                                content = " S ";
                            } else if (p instanceof Peashooter) {
                                content = " P ";
                            } else if (p instanceof Cherrybomb) {
                                content = "C-B";
                            } else if (p instanceof Wallnut) {
                                content = "-W-";
                            } else if (p instanceof PotatoMine) {
                                content = "P-M"; 
                            } else if (p instanceof FreezePeashooter) {
                                content = "F-P";  
                            } else {
                                content = "P??";
                            }
                    } else {
                                // Empty tile
                                content = "---";
                            } 
                    
                }

                // Print tile contents separated by | bars
                System.out.print(content);
                if (c < cols - 1) System.out.print("|");
            }
            System.out.println();
        }
        System.out.println("===================================\n");
    }
}

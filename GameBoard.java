import tiles.Tile;
import plants.Plant;
import plants.Sunflower;
import plants.Peashooter;
import zombies.Zombie;
import zombies.NormalZombie;
import zombies.FlagZombie;
import zombies.ConeheadZombie;

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
     * or "ZC" for {@link ConeheadZombie}.
     */
    public void display() {
        System.out.println("\n======= Current Game Board  =======");
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Tile tile = board[r][c];
                String content = "---";
                Plant p = tile.getPlant();
                boolean hasZombie = tile.hasZombies();

                if (hasZombie) {
                    if (tile.getZombies().size() > 1) {
                        content = " M ";
                    } else {
                        Zombie z = tile.getZombies().get(0);
                        if (z instanceof FlagZombie) {
                            content = "ZF ";
                        } else if (z instanceof ConeheadZombie) {
                            content = "ZC ";
                        } else {
                            content = "ZN ";
                        }
                    }
                } else if (p instanceof Sunflower) {
                    content = " S ";
                } else if (p instanceof Peashooter) {
                    content = " P ";
                }

                System.out.print(content);
                if (c < cols - 1) System.out.print("|");
            }
            System.out.println();
        }
        System.out.println("===================================\n");
    }
}

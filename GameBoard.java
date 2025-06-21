import tiles.Tile;
import plants.Plant;
import plants.Sunflower;
import plants.Peashooter;

public class GameBoard {

    private Tile[][] board;
    private int rows;
    private int cols;

    public GameBoard(Tile[][] board) {
        this.board = board;
        this.rows = board.length;
        this.cols = board[0].length;
    }

    /**
     * Prints the board using ASCII:
     * "---" = empty
     * " S " = Sunflower
     * " P " = Peashooter
     * " Z " = has at least 1 zombie (priority over plant)
     */
    public void display() {
        System.out.println("\n=== Current Game Board ===");
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Tile tile = board[r][c];
                String content = "---";
                Plant p = tile.getPlant();
                boolean hasZombie = tile.hasZombies();

                if (hasZombie) {
                    content = " Z ";
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
        System.out.println("==========================\n");
    }
}

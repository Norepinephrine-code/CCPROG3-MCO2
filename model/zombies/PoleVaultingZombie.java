package model.zombies;
import model.tiles.Tile;

/**
 * Zombie that can leap over the first plant it encounters.
 */
public class PoleVaultingZombie extends Zombie {

    private boolean jump = false;

    /**
     * Creates a pole vaulting zombie at the specified position.
     *
     * @param pos starting tile
     */
    public PoleVaultingZombie (Tile pos) {
        super(70,4,10,pos);
    }

    /** {@inheritDoc} */
    @Override
    public void attack(Tile[][] board) {
        jump(board);
        super.attack(board);
    }

    /**
     * Performs the jump over a plant if it has not jumped yet.
     *
     * @param board current game board
     * @return {@code true} if the zombie already jumped
     */
    public boolean jump(Tile[][] board) {

        if (jump==true) return true;
        boolean hasPlant = this.getPosition().isOccupied();     // isOccupied means it has a plant!

            if (hasPlant == true && jump == false) {

                /** JUMP LOGIC HERE */
                Tile currentTile = this.getPosition();
                int newCol = this.getPosition().getColumn() - 1;   // Move to the left by one
                if (newCol < 0) newCol = 0;                        // Safeguard
                int row = this.getPosition().getRow();
    
                Tile newTile = board[row][newCol];
                listener.onZombieMove(newTile, currentTile, this);
                jump = true;
            } 
        return false;
    }

    /**
     * @return whether this zombie has already performed its vaulting jump
     */
    public boolean hasJumped() {
        return this.jump;
    }

}

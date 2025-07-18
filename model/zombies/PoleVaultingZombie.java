package model.zombies;
import model.tiles.Tile;

public class PoleVaultingZombie extends Zombie {

    private boolean jump = false;

    public PoleVaultingZombie (Tile pos) {
        super(70,4,10,pos);
    }

    @Override
    public void attack(Tile[][] board) {
        jump(board);
        super.attack(board);
    }

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

    public boolean hasJumped() {
        return this.jump;
    }

}

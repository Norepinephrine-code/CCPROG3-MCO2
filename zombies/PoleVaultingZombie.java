package zombies;
import tiles.Tile;

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

    public void jump(Tile[][] board) {

        boolean hasPlant = this.getPosition().isOccupied();     // isOccupied means it has a plant!

            if (hasPlant == true && jump == false) {

                /** JUMP LOGIC HERE */
                Tile currentTile = this.getPosition();
                int newCol = this.getPosition().getColumn() - 1;   // Move to the left by one
                if (newCol < 0) newCol = 0;                        // Safeguard
                int row = this.getPosition().getRow();
    
                currentTile.removeZombie(this);

                Tile newTile = board[row][newCol];
                this.setPosition(newTile);
                newTile.addZombie(this);
                jump = true;
            } 
    }

}

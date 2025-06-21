package plants;
import tiles.Tile;
import zombies.Zombie;

public class Cherrybomb extends Plant {

    public Cherrybomb(Tile position){
        super(150,5.5f,1800,1000,1,1800,5.5f,position);
    }

    /**
     * Overloaded to affect zombies in a 3x3 grid around the Plant position
     * @param zombie A 2D array representing a grid of zombies in that position
     * @return 1 To indicate action was done successfuly
     */
    public int action(Zombie[][] zombie){
        int i, j;
        int plantRow = this.position.getRow();
        int plantCol = this.position.getColumn();
        int rows = zombie.length; // get number of rows of the grid for bound checking
        int columns = zombie[0].length; // get number of columns of the grid for bound checking

        for (i=plantRow-this.range;i<=plantRow+this.range;i++){
            for (j=plantCol-this.range;j<=plantCol+this.range;j++){
                if (i >= 0 && i < rows && j >= 0 && j < columns){
                    if (zombie[i][j] != null){
                        zombie[i][j].takeDamage(this.damage);
                    }
                }
            }
        }
        System.out.println("Cherrybomb exploded at Row " + plantRow + " Column " + plantCol + "!");
        this.health = 0; // destroy Cherrybomb after exploding
        return 1;
    }

}
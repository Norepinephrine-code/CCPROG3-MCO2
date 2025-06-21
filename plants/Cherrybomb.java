package MCO1_Files.Plant;

public class Cherrybomb extends Plant {

    Public Cherrybomb(Tile position){
        super(150,15,1800,1000,1,1800,15,position);
    }

    @Override
    public int action(Zombie[][] zombie){
        int i, j;
        for (i=this.position.row-this.range;this.position.row+range;i++){
            for (j=this.position.column-this.range;j<this.position.column+range;j++){
                if (zombie[i][j].position.row == i && zombie[i][j].position.column == j){
                    zombie.health[i][j] = zombie.health[i][j] - this.damage;
                }

            }
        }
        this.health = 0; // destroy Cherrybomb after exploding
    }

}
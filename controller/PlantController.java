package controller;

import events.GameEventListener;
import model.plants.*;
import model.tiles.Tile;

/**
 * Handles all plant related logic each tick.
 */
public class PlantController {

    private Tile[][] board;
    private GameEventListener listener;

    public PlantController(Tile[][] board, GameEventListener listener) {
        this.board = board;
        this.listener = listener;
    }

    /**
     * Executes plant actions for the current tick.
     *
     * @param ticks current tick count
     * @param sun current available sun
     * @return updated sun amount after plant actions
     */
    public int tick() {
        handleAllPlants();
    }

private void handleAllPlants() {

    for (int r = 0; r < ROWS; r++) {                                                        // ROW
        for (int c = 0; c < COLS; c++) {                                                    // COLUMN
            Plant p = board[r][c].getPlant();                                               // Get Plant per Tile
            
            if (p !=null)
                switch (p.getClass().getSimpleName()) {

                case "Peashooter": 
                //LOGIC HERE CALL ACTION
                    Peashooter ps = (Peashooter) p;
                    ps.shoot(board);
                    break;

                case "Cherrybomb": 
                    Cherrybomb cb = (Cherrybomb) p;
                    // Advance fuse timer and explode when ready
                    cb.tick(board);
                    break;
                
                case "Sunflower":
                    if (ticks % 5 == 0) {           // Adjust Sun generation here
                    Sunflower s = (Sunflower) p;
                    sun = s.action(sun);
                }
                    break;

                case "Wallnut": 
                    // Do nothing!
                    break;

                case "PotatoMine":
                    PotatoMine pm = (PotatoMine) p;
                    pm.armExplode();
                    break;
                case "FreezePeashooter":
                    FreezePeashooter fp = (FreezePeashooter) p;
                    fp.shoot(board);
                    break;

                default: 
                    System.out.println("Unknown Plant Type Error"); 
                    break;// Catcher
                }

        }
    }
}


}
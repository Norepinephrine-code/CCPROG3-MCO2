package controller;

import events.GameEventListener;
import model.plants.*;
import model.tiles.Tile;

/*
 *
░█████████  ░██            ░███    ░███    ░██ ░██████████     ░██████    ░██████   ░███    ░██ ░██████████░█████████    ░██████   ░██         ░██         ░██████████ ░█████████
░██     ░██ ░██           ░██░██   ░████   ░██     ░██        ░██   ░██  ░██   ░██  ░████   ░██     ░██    ░██     ░██  ░██   ░██  ░██         ░██         ░██         ░██     ░██ 
░██     ░██ ░██          ░██  ░██  ░██░██  ░██     ░██       ░██        ░██     ░██ ░██░██  ░██     ░██    ░██     ░██ ░██     ░██ ░██         ░██         ░██         ░██     ░██ 
░█████████  ░██         ░█████████ ░██ ░██ ░██     ░██       ░██        ░██     ░██ ░██ ░██ ░██     ░██    ░█████████  ░██     ░██ ░██         ░██         ░█████████  ░█████████  
░██         ░██         ░██    ░██ ░██  ░██░██     ░██       ░██        ░██     ░██ ░██  ░██░██     ░██    ░██   ░██   ░██     ░██ ░██         ░██         ░██         ░██   ░██   
░██         ░██         ░██    ░██ ░██   ░████     ░██        ░██   ░██  ░██   ░██  ░██   ░████     ░██    ░██    ░██   ░██   ░██  ░██         ░██         ░██         ░██    ░██  
░██         ░██████████ ░██    ░██ ░██    ░███     ░██         ░██████    ░██████   ░██    ░███     ░██    ░██     ░██   ░██████   ░██████████ ░██████████ ░██████████ ░██     ░██ 
                                                                                                                                                                                   
    - Handles all the plant model manipulation and calls
 */
/**
 * Controls plant behaviour each tick, delegating actions and notifying the
 * {@link GameEventListener} of any state changes.
 */
public class PlantController {

    private final Tile[][] board;
    private final GameEventListener listener;
    private final int ROWS;
    private final int COLS;

    /**
     * Creates a controller to manage all plants on the board.
     *
     * @param board     game board tiles
     * @param listener  event listener to notify
     * @param ROWS      number of rows in the board
     * @param COLS      number of columns in the board
     */
    public PlantController(Tile[][] board, GameEventListener listener, int ROWS, int COLS) {
        this.board = board;
        this.listener = listener;
        this.ROWS = ROWS;
        this.COLS = COLS;
    }

    /**
     * Executes all plant behaviours for this game tick.
     *
     * @param ticks current tick count
     * @return {@code true} if the time limit has been reached
     */
    public boolean tick(int ticks) {
        if (ticks==180) return true;
        handleAllPlants(ticks); return false;
    }

    /**
     * Iterates over every plant on the board and triggers its behaviour for the
     * current tick.
     *
     * @param ticks current tick count
     */
    private void handleAllPlants(int ticks) {

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
                    Sunflower sf = (Sunflower) p;
                    sf.action();
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
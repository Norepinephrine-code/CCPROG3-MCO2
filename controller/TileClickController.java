package controller;

import events.GameEventListener;
import javax.swing.JOptionPane;
import model.plants.Cherrybomb;
import model.plants.FreezePeashooter;
import model.plants.Peashooter;
import model.plants.Plant;
import model.plants.PotatoMine;
import model.plants.Sunflower;
import model.plants.Wallnut;
import model.tiles.Tile;

public class TileClickController {

    private Game game;
    private Tile[][] board;
    private GameEventListener listener;
    
    private final int ROWS;
    private final int COLS;
    private static final int HOUSE_COLUMN = 0;
    private static final int FIRST_PLANTABLE_COLUMN = HOUSE_COLUMN + 1;

    public TileClickController(Game game, Tile[][] board, GameEventListener listener, int ROWS, int COLS) {
        this.game = game;
        this.board = board;
        this.listener = listener;
        this.ROWS = ROWS;
        this.COLS = COLS;
    }

    public void action(int clicked_row, int clicked_column) {
       handleTileClick(clicked_row, clicked_column);
    }

    public void handleTileClick(int clicked_row, int clicked_column) {

        Tile t = board[clicked_row][clicked_column];
        Plant p = t.getPlant();
        int selectedPlant = game.getSelectedPlantType();

        if (t.hasSunDrop()) {
            listener.onCollectSunFromTile(t);
            game.setSelectedPlantType(0);
            return;
        }

        if (p instanceof Sunflower && selectedPlant != 7) {
            Sunflower sf = (Sunflower) p;
            sf.collect(); 
            game.setSelectedPlantType(0);
        } else {
            placePlant(clicked_row, clicked_column, selectedPlant);
            game.setSelectedPlantType(0);
        }
    }

    private void placePlant(int clicked_row, int clicked_column, int selectedPlant) {

        // CHECKERS BELOW
        int sun = game.getSun();
        if (selectedPlant==0) return;

        // The purpose of the validator is to check if there is an existing plant.
        // We want to ignore the validator if we are intending to remove

        if (!isValidPosition(clicked_row,clicked_column) && selectedPlant!=7) return;
        if (!isValidPurchase(selectedPlant,sun)) return;         

        Tile tilePlant = board[clicked_row][clicked_column];

            switch (selectedPlant) {

            case 1:    // SUNFLOWER
                tilePlant.setPlant(new Sunflower(tilePlant));
                tilePlant.getPlant().setGameEventListener(this.listener);
                break;

            case 2:     // PEASHOOTER
                tilePlant.setPlant(new Peashooter(tilePlant));
                tilePlant.getPlant().setGameEventListener(this.listener);
                break;

            case 3:     // CHERRYBOMB
                tilePlant.setPlant(new Cherrybomb(tilePlant));
                tilePlant.getPlant().setGameEventListener(this.listener);
                break;

            case 4:     // WALLNUT
                tilePlant.setPlant(new Wallnut(tilePlant));
                tilePlant.getPlant().setGameEventListener(this.listener);
                break;
            case 5:     // POTATO MINE
                tilePlant.setPlant(new PotatoMine(tilePlant));
                tilePlant.getPlant().setGameEventListener(this.listener);
                break;
            case 6:     // FREEZE PEA
                tilePlant.setPlant(new FreezePeashooter(tilePlant));
                tilePlant.getPlant().setGameEventListener(this.listener);
                break;
            case 7:     // REMOVE PLANT
                if (tilePlant.hasPlant()) {
                    listener.onPlantRemoved(tilePlant.getPlant());
                    System.out.println("Plant removed!");
                } else {
                    System.out.println("There is no plant to remove!");
                }
                break;

            default:
                break;
        }

        if (tilePlant.getPlant()!=null) {
            System.out.println("Placed " + tilePlant.getPlant().getClass().getSimpleName() +
                " at Row " + (clicked_row + 1) + ", Column " + (clicked_column + 1));

            listener.onSetPlant(tilePlant.getPlant()); // Tell Game that a Plant has been set
        }
    }

    private boolean isValidPosition(int r, int c) {               // Helper Method

        if ((r >= 0 && r < ROWS && c >= FIRST_PLANTABLE_COLUMN && c < COLS && !board[r][c].isOccupied())) {
            return true;
        } else {
            System.out.println("Invalid location!");
            return false;
        }
    }

    private boolean isValidPurchase(int selectedPlant, int sun) {         // Helper Method
        int cost;

        switch (selectedPlant) {
            case 1: cost = 50; break;   // Sunflower
            case 2: cost = 100; break;  // Peashooter
            case 3: cost = 150; break;  // Cherrybomb
            case 4: cost = 50; break;   // Wallnut
            case 5: cost = 25; break;   // PotatoMine
            case 6: cost = 175; break;  // FreezePeashooter
            case 7: cost = 0; break;    // Remove (free)
            default:
                System.out.println("Unknown Plant!");
                return false;
        }

        if (sun >= cost) {
            sun -= cost;
            game.setSun(sun);
            return true;
        } else {
            System.out.println("Not enough sun!");
            JOptionPane.showMessageDialog(null, "You don't have enough sun!", "Sorry", JOptionPane.WARNING_MESSAGE);
            return false;
        }
    }

    
}

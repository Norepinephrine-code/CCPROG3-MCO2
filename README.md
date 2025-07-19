# Plants vs Zombies Java Implementation

This repository contains a small Java recreation of **Plants vs Zombies** for a class project. The game can be played entirely in the console or using the included Swing graphical interface.

## Running

1. Compile all sources:
   ```bash
   javac */*.java */*/*.java
   ```
2. Start the program:
   ```bash
   java Driver
   ```
   The level selection window will appear and the game will launch once you choose a level.

## Source Overview

The table below lists every Java file in the project along with its responsibility.

| File | Responsibility |
| ---- | -------------- |
| `Driver.java` | Entry point that launches the `LevelSelectorGUI`. |
| `controller/Game.java` | Coordinates the entire game, manages timers, board state and event callbacks. |
| `controller/PlantController.java` | Executes actions for all plants each tick. |
| `controller/TileClickController.java` | Handles mouse clicks to place or remove plants and collect sun. |
| `controller/ZombieController.java` | Spawns zombies and moves them according to level rules. |
| `events/GameEventListener.java` | Interface for notifying about kills, moves, and sun events. |
| `model/plants/Plant.java` | Base class containing shared plant attributes and behaviour. |
| `model/plants/Sunflower.java` | Generates sun over time when collected. |
| `model/plants/Peashooter.java` | Fires peas at zombies from a distance. |
| `model/plants/FreezePeashooter.java` | Variant that freezes zombies in addition to dealing damage. |
| `model/plants/Cherrybomb.java` | Explodes after a short fuse damaging nearby zombies. |
| `model/plants/Wallnut.java` | Defensive plant acting as a high-health barrier. |
| `model/plants/PotatoMine.java` | Arms after a delay and explodes when a zombie steps on it. |
| `model/tiles/Tile.java` | Represents a single board square containing a plant and/or zombies. |
| `model/zombies/Zombie.java` | Abstract superclass for all zombies providing movement and attack logic. |
| `model/zombies/NormalZombie.java` | Standard zombie with balanced attributes. |
| `model/zombies/FlagZombie.java` | Faster zombie that signals the arrival of a wave. |
| `model/zombies/ConeheadZombie.java` | Slower but tougher zombie wearing a traffic cone. |
| `model/zombies/BucketHeadZombie.java` | Extremely durable zombie wearing a bucket. |
| `model/zombies/PoleVaultingZombie.java` | Can jump over the first plant it encounters. |
| `util/MusicPlayer.java` | Utility for looping background music during gameplay. |
| `view/GameBoard.java` | Prints a text representation of the board to the console. |
| `view/GameBoardGUI.java` | Swing GUI that visually displays the board and indicators. |
| `view/LevelSelectorGUI.java` | Simple window that lets you choose a game level. |
| `view/TileLabel.java` | Custom Swing label used for each tile in the GUI grid. |

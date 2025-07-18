public class PlantingRequest {
    public final int row;
    public final int col;
    public final PlantType type;

    public PlantingRequest(int row, int col, PlantType type) {
        this.row = row;
        this.col = col;
        this.type = type;
    }
}

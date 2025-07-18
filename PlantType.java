public enum PlantType {
    SUNFLOWER(50),
    PEASHOOTER(100),
    CHERRYBOMB(150),
    WALLNUT(50),
    POTATOMINE(25),
    FREEZEPEASHOOTER(100);

    public final int cost;
    PlantType(int cost){
        this.cost = cost;
    }
}

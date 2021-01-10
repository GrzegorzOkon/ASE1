package okon.ASE1;

public class Space {
    private final String database;
    private final Float freeData;
    private final Float freePercentData;

    public Space(String database, Float freeData, Float freePercentData) {
        this.database = database;
        this.freeData = freeData;
        this.freePercentData = freePercentData;
    }
}

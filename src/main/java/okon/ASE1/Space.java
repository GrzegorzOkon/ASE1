package okon.ASE1;

public class Space {
    private final String database;
    private final Float freeData;
    private final Float freePercentData;
    private final Float freeLog;
    private final Float freePercentLog;

    public Space(String database, Float freeData, Float freePercentData, Float freeLog, Float freePercentLog) {
        this.database = database;
        this.freeData = freeData;
        this.freePercentData = freePercentData;
        this.freeLog = freeLog;
        this.freePercentLog = freePercentLog;
    }

    public String getDatabase() {
        return database;
    }

    public Float getFreeData() {
        return freeData;
    }

    public Float getFreePercentData() {
        return freePercentData;
    }

    public Float getFreeLog() {
        return freeLog;
    }

    public Float getFreePercentLog() {
        return freePercentLog;
    }
}

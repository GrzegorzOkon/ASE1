package okon.ASE1;

public class Space {
    private final String database;
    private final Integer freeData;
    private final Float freePercentData;
    private final Integer freeLog;
    private final Float freePercentLog;

    public Space(String database, Integer freeData, Float freePercentData, Integer freeLog, Float freePercentLog) {
        this.database = database;
        this.freeData = freeData;
        this.freePercentData = freePercentData;
        this.freeLog = freeLog;
        this.freePercentLog = freePercentLog;
    }

    public String getDatabase() {
        return database;
    }

    public Integer getFreeData() {
        return freeData;
    }

    public Float getFreePercentData() {
        return freePercentData;
    }

    public Integer getFreeLog() {
        return freeLog;
    }

    public Float getFreePercentLog() {
        return freePercentLog;
    }
}

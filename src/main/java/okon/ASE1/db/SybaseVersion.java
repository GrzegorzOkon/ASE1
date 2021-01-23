package okon.ASE1.db;

public enum SybaseVersion {
    ASE_12530 { public int[] getProcedureLastRSIndexes() { return new int[] {4, 5}; }},
    ASE_15030 { public int[] getProcedureLastRSIndexes() { return new int[] {5, 6}; }},
    ASE_15700 { public int[] getProcedureLastRSIndexes() { return new int[] {5, 5}; }};
    public abstract int[] getProcedureLastRSIndexes();
}
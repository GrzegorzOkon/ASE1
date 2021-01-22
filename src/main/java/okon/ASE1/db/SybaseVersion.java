package okon.ASE1.db;

public enum SybaseVersion {
    ASE_12530 { public int[] getProcedureLastRSIndex() { return new int[] {5, 6}; }},
    ASE_15030 { public int[] getProcedureLastRSIndex() { return new int[] {5, 6}; }},
    ASE_15700 { public int[] getProcedureLastRSIndex() { return new int[] {5, 5}; }};
    public abstract int[] getProcedureLastRSIndex();
}
package okon.ASE1.db;

public interface Gateway extends AutoCloseable {
    public String[] getDatabaseNames();

    @Override
    void close() throws Exception;
}

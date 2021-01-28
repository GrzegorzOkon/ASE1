package okon.ASE1.db;

import java.util.List;

public interface Gateway extends AutoCloseable {
    public List<String> getDatabaseNames();

    public List<Integer> getDataSpace(String database);

    public List<Integer> getLogSpace(String database);

    @Override
    void close() throws Exception;
}

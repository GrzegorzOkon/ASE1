package okon.ASE1.db;

import java.util.List;

public interface Gateway extends AutoCloseable {
    public List<String> getDatabaseNames();

    public List<String> getDataSpace(String database);

    public List<String> getLogSpace(String database);

    @Override
    void close() throws Exception;
}

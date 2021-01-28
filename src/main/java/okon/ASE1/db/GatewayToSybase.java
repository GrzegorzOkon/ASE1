package okon.ASE1.db;

import com.sybase.jdbc4.jdbc.SybDataSource;
import okon.ASE1.Job;
import okon.ASE1.exception.ConnectionException;

import java.io.Closeable;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.sql.DataSource;

import static okon.ASE1.ASE1App.getJarFileName;

public class GatewayToSybase implements Closeable, Gateway {
    private final Connection db;
    private final SybaseVersion version;
    private final int pageSize;

    public GatewayToSybase(Job job) {
        try {
            db = createDataSource(job.getServer().getIp(), job.getServer().getPort(), job.getAuthorization().getUsername(), job.getAuthorization().getPassword()).getConnection();
            version = getVersion();
            pageSize = getPageSize();
        } catch (SQLException throwables) {
            throw new ConnectionException(throwables);
        }
    }

    private DataSource createDataSource (String serverName, int portNumber, String user, String password){
        SybDataSource dataSource = new SybDataSource();
        dataSource.setServerName(serverName);
        dataSource.setPortNumber(portNumber);
        dataSource.setUser(user);
        dataSource.setPassword(password);
        dataSource.setAPPLICATIONNAME(getJarFileName());
        return dataSource;
    }

    private SybaseVersion getVersion() {
        String version = "ASE_";
        String query = "select @@version_number";
        try (Statement stmt = db.createStatement(1004, 1007); ResultSet rs = stmt.executeQuery(query);) {
            while (rs.next()) {
                version += rs.getInt(1);
            }
        } catch (SQLException throwables) {
            throw new ConnectionException(throwables);
        }
        return SybaseVersion.valueOf(version);
    }

    private int getPageSize() {
        int result = 0;
        String query = "select @@maxpagesize";
        try (Statement stmt = db.createStatement(1004, 1007); ResultSet rs = stmt.executeQuery(query);) {
            while (rs.next()) {
                result += rs.getInt(1);
            }
        } catch (SQLException throwables) {
            throw new ConnectionException(throwables);
        }
        return result;
    }

    @Override
    public List<String> getDatabaseNames() {
        List<String> result = new ArrayList<>();
        String query = null;
        if (version == SybaseVersion.ASE_15700) {
            query = "select name from master..sysdatabases where instanceid in (NULL, 0, @@instanceid)";
        } else {
            query = "select name from master..sysdatabases";
        }
        try (Statement stmt = db.createStatement(1004, 1007); ResultSet rs = stmt.executeQuery(query);) {
            while (rs.next()) {
                result.add(rs.getString(1));
            }
        } catch (SQLException throwables) {
            throw new ConnectionException(throwables);
        }
        return result;
    }

    @Override
    public List<Integer> getDataSpace(String database) {
        List<String> result = new ArrayList<>();
        try (CallableStatement cstmt = db.prepareCall("{call sp_helpsegment('default')}");) {
            db.setCatalog(database);
            db.setTransactionIsolation(1);
            cstmt.setQueryTimeout(60);
            boolean res = cstmt.execute();
            for (int i = 1; res && i <= version.getProcedureLastRSIndexes()[0]; i++) {
                if (isLastResultSet(i, version.getProcedureLastRSIndexes()[0])) {
                    ResultSet rs = cstmt.getResultSet();
                    result = getSpace(rs);
                }
                res = cstmt.getMoreResults();
            }
        } catch (SQLException throwables) {
            throw new ConnectionException(throwables);
        }
        return convertPagesToBytes(result);
    }

    @Override
    public List<Integer> getLogSpace(String database) {
        List<String> result = null;
        try (CallableStatement cstmt = db.prepareCall("{call sp_helpsegment('logsegment')}");) {
            db.setCatalog(database);
            db.setTransactionIsolation(1);
            cstmt.setQueryTimeout(60);
            boolean res = cstmt.execute();
            for (int i = 1; res && i <= version.getProcedureLastRSIndexes()[1]; i++) {
                if (isLastResultSet(i, version.getProcedureLastRSIndexes()[1])) {
                    ResultSet rs = cstmt.getResultSet();
                    result = getSpace(rs);
                }
                res = cstmt.getMoreResults();
            }
        } catch (SQLException throwables) {
            throw new ConnectionException(throwables);
        }
        return convertPagesToBytes(result);
    }

    private List<String> getSpace(ResultSet rs) {
        String totalPages = null;
        String freePages = null;
        try {
            while (rs.next()) {
                totalPages = rs.getString(2);
                freePages = rs.getString(3);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Arrays.asList(totalPages, freePages);
    }

    private boolean isLastResultSet(int counter, int lastIndex) {
        if (counter == lastIndex) return true;
        return false;
    }

    private List<Integer> convertPagesToBytes(List<String> pageSpace) {
        List<Integer> result = new ArrayList<>();
        for (String pages : pageSpace) {
            result.add(Integer.valueOf(pages) * pageSize);
        }
        return result;
    }

    @Override
    public void close() {
        try {
            db.close();
        } catch (SQLException throwables) {
            throw new ConnectionException(throwables);
        }
    }
}
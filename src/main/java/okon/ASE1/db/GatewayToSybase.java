package okon.ASE1.db;

import com.sybase.jdbc4.jdbc.SybDataSource;
import okon.ASE1.Job;
import okon.ASE1.exception.ConnectionException;

import java.io.Closeable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

import static okon.ASE1.ASE1App.getJarFileName;

public class GatewayToSybase implements Closeable, Gateway {
    private final int ASE_XX_RS_COUNT = 5;
    private Connection db;

    public GatewayToSybase(Job job) {
        try {
            db = createDataSource(job.getServer().getIp(), job.getServer().getPort(), job.getAuthorization().getUsername(), job.getAuthorization().getPassword()).getConnection();
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

    @Override
    public List<String> getDatabaseNames() {
        List<String> result = new ArrayList<>();
        String query = "select name from master..sysdatabases where instanceid in (NULL, 0, @@instanceid)";
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
    public List<String> getDataSpace(String database) {
        List<String> result = new ArrayList<>();
        try (CallableStatement cstmt = db.prepareCall("{call sp_helpsegment('default')}");) {
            db.setCatalog(database);
            db.setTransactionIsolation(1);
            cstmt.setQueryTimeout(60);
            boolean res = cstmt.execute();
            for (int i = 1; res && i <= ASE_XX_RS_COUNT; i++) {
                if (isLastResultSet(i)) {
                    ResultSet rs = cstmt.getResultSet();
                    result = getSpace(rs);
                }
                res = cstmt.getMoreResults();
            }
        } catch (SQLException throwables) {
            throw new ConnectionException(throwables);
        }
        return result;
    }

    @Override
    public List<String> getLogSpace(String database) {
        List<String> result = new ArrayList<>();
        try (CallableStatement cstmt = db.prepareCall("{call sp_helpsegment('logsegment')}");) {
            db.setCatalog(database);
            db.setTransactionIsolation(1);
            cstmt.setQueryTimeout(60);
            boolean res = cstmt.execute();
            for (int i = 1; res && i <= ASE_XX_RS_COUNT; i++) {
                if (isLastResultSet(i)) {
                    ResultSet rs = cstmt.getResultSet();
                    result = getSpace(rs);
                }
                res = cstmt.getMoreResults();
            }
        } catch (SQLException throwables) {
            throw new ConnectionException(throwables);
        }
        return result;
    }

    private List<String> getSpace(ResultSet rs) {
        List<String> result = new ArrayList<>();
        try {
            while (rs.next()) {
                result.add(rs.getString(1));
                result.add(rs.getString(2));
                result.add(rs.getString(3));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }

    private boolean isLastResultSet(int counter) {
        if (counter == ASE_XX_RS_COUNT) return true;
        return false;
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
package okon.ASE1.db;

import com.sybase.jdbc4.jdbc.SybDataSource;
import okon.ASE1.Job;
import okon.ASE1.exception.ConnectionException;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;

import static okon.ASE1.ASE1App.getJarFileName;

public class GatewaySybase implements Closeable, Gateway {
    private Connection db;

    public GatewaySybase(Job job) {
        try {
            db = createDataSource(job.getServer().getIp(), job.getServer().getPort(), job.getAuthorization().getUsername(), job.getAuthorization().getPassword()).getConnection();
        } catch (SQLException e) {
            throw new ConnectionException(e);
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
    public String[] getDatabaseNames() {
        String[] result;
        String query = "select name from master..sysdatabases where instanceid in (0, NULL, @@instanceid)";
        int i = 0;
        try (Statement stmt = db.createStatement(1004, 1007); ResultSet rs = stmt.executeQuery(query);) {
            int rowcount = 0;
            if (rs.last()) {
                rowcount = rs.getRow();
                rs.beforeFirst();
            }
            if (rowcount == 0)
                return null;
            result = new String[rowcount];
            while (rs.next()) {
                result[i] = rs.getString(1);
                i++;
            }
        } catch (SQLException e) {
            throw new ConnectionException(e);
        }
        return result;
    }

    @Override
    public void close() {
        try {
            db.close();
        } catch (SQLException e) {
            throw new ConnectionException(e);
        }
    }
}
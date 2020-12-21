package okon.ASE1.db;

import com.sybase.jdbc4.jdbc.SybDataSource;
import okon.ASE1.Job;
import okon.ASE1.exception.AppException;
import okon.ASE1.exception.ConnectionException;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;

public class GatewaySybase implements Closeable {
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
        dataSource.setAPPLICATIONNAME("ASE1");
        return dataSource;
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

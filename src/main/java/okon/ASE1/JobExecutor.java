package okon.ASE1;

import okon.ASE1.db.GatewayFactory;
import okon.ASE1.db.GatewaySybase;
import okon.ASE1.exception.AppException;
import okon.ASE1.exception.ConnectionException;

import java.util.ArrayList;
import java.util.List;

public class JobExecutor {
    public static List<Extraction> execute(Job job) {
        List<Extraction> result = new ArrayList<>();
        try (GatewaySybase db = GatewayFactory.make(job)) {

        } catch (ConnectionException e) {

        } catch (Exception e) {
            throw new AppException(e);
        }
        return result;
    }
}

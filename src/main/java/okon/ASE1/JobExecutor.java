package okon.ASE1;

import okon.ASE1.db.Gateway;
import okon.ASE1.db.GatewayFactory;
import okon.ASE1.exception.AppException;
import okon.ASE1.exception.ConnectionException;
import okon.ASE1.service.SpaceService;

import java.util.List;

public class JobExecutor {
    public static List<Space> execute(Job job) {
        List<Space> result = null;
        try (Gateway db = GatewayFactory.make(job)) {
            SpaceService service = new SpaceService(db);
            result = service.getDatabaseSpace(job);
        } catch (ConnectionException e) {

        } catch (Exception e) {
            throw new AppException(e);
        }
        return result;
    }
}
package okon.ASE1.service;

import okon.ASE1.Space;
import okon.ASE1.Job;
import okon.ASE1.db.Gateway;

import java.util.ArrayList;
import java.util.List;

public class SpaceService {
    private Gateway db;

    public SpaceService(Gateway db) {
        this.db = db;
    }

    public List<Space> getDatabaseSpace(Job job) {
        List<String> databases = db.getDatabaseNames();
        return getDatabaseSpace(databases);
    }

    private List<Space> getDatabaseSpace(List<String> databases) {
        List<Space> result = new ArrayList<>();
        for (String database : databases) {
            List<Integer> dataSpace = db.getDataSpace(database);
            List<Integer> logSpace = db.getLogSpace(database);
            result.add(new Space(database, dataSpace.get(0), calculateFreePercentSpace(dataSpace),
                    logSpace.get(0), calculateFreePercentSpace(logSpace)));
        }
        return result;
    }

    private Float calculateFreePercentSpace(List<Integer> space) {
        return space.get(1).floatValue() / space.get(0).floatValue() * 100.0f;
    }
}
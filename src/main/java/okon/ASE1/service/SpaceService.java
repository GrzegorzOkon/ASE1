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
            List<String> dataSpace = db.getDataSpace(database);
            System.out.println(dataSpace);
        }
        return result;
    }
}

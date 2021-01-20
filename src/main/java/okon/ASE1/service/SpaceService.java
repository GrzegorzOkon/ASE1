package okon.ASE1.service;

import okon.ASE1.Space;
import okon.ASE1.Job;
import okon.ASE1.db.Gateway;

import java.util.ArrayList;
import java.util.Arrays;
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
            List<String> logSpace = db.getLogSpace(database);
            List<Float> calculatedDataSpace = calculateFreeSpace(dataSpace);
            List<Float> calculatedLogSpace = calculateFreeSpace(logSpace);
            result.add(new Space(database, calculatedDataSpace.get(0), calculatedDataSpace.get(1),
                    calculatedLogSpace.get(0), calculatedLogSpace.get(1)));
        }
        return result;
    }

    private List<Float> calculateFreeSpace(List<String> data) {
        Float totalSize = Float.valueOf(data.get(0).trim().replace("MB", "0"));
        Integer totalPages = Integer.valueOf(data.get(1).trim());
        Integer freePages = Integer.valueOf(data.get(2).trim());
        Float freeSpace = freePages.floatValue() / totalPages.floatValue() * totalSize.floatValue();
        Float freePercentSpace = freePages.floatValue() / totalPages.floatValue() * 100.0f;
        return Arrays.asList(freeSpace, freePercentSpace);
    }
}
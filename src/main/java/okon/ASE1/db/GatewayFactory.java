package okon.ASE1.db;

import okon.ASE1.Job;

public class GatewayFactory {
    public static GatewayToSybase make(Job job) {
        return new GatewayToSybase(job);
    }
}

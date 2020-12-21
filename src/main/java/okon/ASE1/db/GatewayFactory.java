package okon.ASE1.db;

import okon.ASE1.Job;

public class GatewayFactory {
    public static GatewaySybase make(Job job) {
        return new GatewaySybase(job);
    }
}

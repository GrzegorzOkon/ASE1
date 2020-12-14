package okon.ASE1;

import okon.ASE1.config.AuthorizationConfigReader;
import okon.ASE1.config.ServerConfigReader;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ASE1App {
    static final Queue<Job> jobs = new LinkedList<>();

    public static void main(String[] args) {
        initializeQueue();
    }

    static void initializeQueue() {
        List<Server> servers = ServerConfigReader.readParams((new File("./config/servers.xml")));
        List<Authorization> authorizations = AuthorizationConfigReader.readParams((new File("./config/server-auth.xml")));
        createJobs(servers, authorizations);
    }

    static void createJobs(List<Server> servers, List<Authorization> authorizations) {
        for (Server server : servers) {
            Job job = new Job(server, matchAuthorizationToServer(server, authorizations));
            jobs.add(job);
        }
    }

    static Authorization matchAuthorizationToServer(Server server, List<Authorization> authorizations) {
        if (isAuthorizationPresent(server)) {
            for (Authorization authorization : authorizations) {
                if (server.getAuthorizationInterface().equals(authorization.getAuthorizationInterface())) {
                    return authorization;
                }
            }
        }
        return null;
    }

    static boolean isAuthorizationPresent(Server server) {
        if (!server.getAuthorizationInterface().equals("")) {
            return true;
        }
        return false;
    }
}

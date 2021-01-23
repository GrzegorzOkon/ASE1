package okon.ASE1;

import okon.ASE1.config.AuthorizationConfigReader;
import okon.ASE1.config.ServerConfigReader;
import okon.ASE1.exception.AppException;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ASE1App {
    static final Queue<Job> jobs = new LinkedList<>();
    static final List<Result> extractions = new ArrayList();

    static {
        initializeQueue();
    }

    public static void main(String[] args) {
        new ASE1App().startThreadPool(jobs.size());
        new ReportPrinter().print(extractions);
    }

    static void initializeQueue() {
        List<Server> servers = ServerConfigReader.readParams((new File("./config/servers.xml")));
        List<Authorization> authorizations = AuthorizationConfigReader.readParams((new File("./config/authorizations.xml")));
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

    private void startThreadPool(int threadSum) {
        Thread[] threads = new Thread[threadSum];
        for (int i = 0; i < threadSum; i++) {
            threads[i] = new JobConsumentThread();
        }
        for (int i = 0; i < threadSum; i++) {
            threads[i].start();
        }
        for (int i = 0; i < threadSum; i++) {
            try {
                threads[i].join();
            } catch (Exception e) {
                throw new AppException(e);
            }
        }
    }

    public static String getJarFileName() {
        String path = ASE1App.class.getResource(ASE1App.class.getSimpleName() + ".class").getFile();
        path = path.substring(0, path.lastIndexOf('!'));
        path = path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf('.'));
        return path;
    }
}
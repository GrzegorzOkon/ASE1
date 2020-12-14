package okon.ASE1;

public class Job {
    private final Server server;
    private final Authorization authorization;

    public Job(Server server, Authorization authorization) {
        this.server = server;
        this.authorization = authorization;
    }

    public Server getServer() {
        return server;
    }

    public Authorization getAuthorization() {
        return authorization;
    }
}

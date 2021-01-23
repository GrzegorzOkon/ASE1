package okon.ASE1;

import java.util.List;

public class Result {
    private final Server server;
    private final List<Space> space;

    public Result(Server server, List<Space> space) {
        this.server = server;
        this.space = space;
    }

    public Server getServer() {
        return server;
    }

    public List<Space> getSpace() {
        return space;
    }
}

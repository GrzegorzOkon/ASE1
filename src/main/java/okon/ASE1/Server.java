package okon.ASE1;

public class Server {
    private final String alias;
    private final String ip;
    private final Integer port;
    private final String vendor;
    private final String authorizationInterface;

    public Server(String alias, String ip, Integer port, String vendor, String authorizationInterface) {
        this.alias = alias;
        this.ip = ip;
        this.port = port;
        this.vendor = vendor;
        this.authorizationInterface = authorizationInterface;
    }

    public String getAlias() {
        return alias;
    }

    public String getIp() {
        return ip;
    }

    public Integer getPort() {
        return port;
    }

    public String getVendor() {
        return vendor;
    }

    public String getAuthorizationInterface() {
        return authorizationInterface;
    }
}

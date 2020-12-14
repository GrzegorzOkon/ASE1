package okon.ASE1;

public class Authorization {
    private final String authorizationInterface;
    private final String username;
    private final String password;

    public Authorization(String authorizationInterface, String username, String password) {
        this.authorizationInterface = authorizationInterface;
        this.username = username;
        this.password = password;
    }

    public String getAuthorizationInterface() {
        return authorizationInterface;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}

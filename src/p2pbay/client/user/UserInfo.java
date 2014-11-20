package p2pbay.client.user;

import p2pbay.client.Client;

public class UserInfo {
    public static String USERNAME = "Username:";
    public static String PASSWORD = "Password:";

    private Client client;

    public Client getClient() {
        return client;
    }

    public UserInfo(Client client) {
        this.client = client;
    }

    protected String getUsername() {
        System.out.print(USERNAME);
        return client.getInput();
    }

    protected String getPassword() {
        System.out.print(PASSWORD);
        return client.getInput();
    }
}

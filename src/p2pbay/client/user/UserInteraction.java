package p2pbay.client.user;

import p2pbay.client.Client;

public class UserInteraction {
    public static String USERNAME = "Username:";
    public static String PASSWORD = "Password:";

    private Client client;

    public Client getClient() {
        return client;
    }

    public UserInteraction(Client client) {
        this.client = client;
    }

    public String getUsername() {
        System.out.print(USERNAME);
        return client.getInput();
    }

    public String getPassword() {
        System.out.print(PASSWORD);
        return client.getInput();
    }

    public String getInput() {
        return client.getInput();
    }

    public float getFloat() {
        return client.getNumberInput();
    }
}

package p2pbay.client.user;

import p2pbay.client.Client;

public abstract class UserInteraction implements Runnable {
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

    public float getPositiveNumber(String message) {
        while (true) {
            System.out.print(message);
            try {
                float value = client.getNumberInput();

                if (value > 0)
                    return value;
            } catch (NumberFormatException ignored) {}

            System.out.println("O valor introduzido nao e valido.");
        }
    }

    public abstract void getInfo();

    public abstract void storeObjects();

    @Override
    public void run() {
        getInfo();
        storeObjects();
    }
}

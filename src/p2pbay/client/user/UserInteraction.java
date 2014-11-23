package p2pbay.client.user;

import p2pbay.client.Client;
import p2pbay.client.SysStrings;

public abstract class UserInteraction implements Runnable {

    private Client client;

    public Client getClient() {
        return client;
    }

    public UserInteraction(Client client) {
        this.client = client;
    }

    public String getUsername() {
        System.out.print(SysStrings.INPUT_USERNAME);
        return client.getInput();
    }

    public String getPassword() {
        System.out.print(SysStrings.INPUT_USERNAME);
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

            System.out.println(SysStrings.INVALID_VALUE);
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

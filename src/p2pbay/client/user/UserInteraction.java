package p2pbay.client.user;

import p2pbay.client.Client;
import p2pbay.client.SysStrings;
import java.io.Console;

public abstract class UserInteraction implements Runnable {

    private Client client;

    public Client getClient() {
        return client;
    }

    public UserInteraction(Client client) {
        this.client = client;
    }

    public String getUsername() {
        String username = null;
        try {
            username = getClient().readInput(SysStrings.INPUT_USERNAME);
        } catch(Exception e){
            // if any error occurs
            e.printStackTrace();
        } finally {
            return username;
        }
    }

    public String getPassword() {
        String password = null;
        try {
            password = getClient().readInputPassword();
        } catch(Exception e){
            // if any error occurs
            e.printStackTrace();
        } finally {
            return password;
        }
    }

    public String getInput() {
        return client.readInput();
    }

    public float getFloat() {
        return client.readNumberInput();
    }

    public float getPositiveNumber(String message) {
        while (true) {
            System.out.print(message);
            try {
                float value = client.readNumberInput();

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

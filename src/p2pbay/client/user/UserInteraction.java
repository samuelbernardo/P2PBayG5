package p2pbay.client.user;

import p2pbay.client.Client;
import java.io.Console;

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
        String username = null;
        try {
            // creates a console object
            Console cnsl = System.console();
            // if console is not null
            if (cnsl != null) {
                username = cnsl.readLine(USERNAME);
            }
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
            // creates a console object
            Console cnsl = System.console();
            // if console is not null
            if (cnsl != null) {
                password = new String(cnsl.readPassword(PASSWORD));
            }
        } catch(Exception e){
            // if any error occurs
            e.printStackTrace();
        } finally {
            return password;
        }
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

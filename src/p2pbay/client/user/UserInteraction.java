package p2pbay.client.user;

import p2pbay.client.Client;
<<<<<<< HEAD
import p2pbay.client.SysStrings;
=======
import java.io.Console;
>>>>>>> refs/remotes/origin/pre_release

public abstract class UserInteraction implements Runnable {

    private Client client;

    public Client getClient() {
        return client;
    }

    public UserInteraction(Client client) {
        this.client = client;
    }

    public String getUsername() {
<<<<<<< HEAD
        System.out.print(SysStrings.INPUT_USERNAME);
        return client.getInput();
=======
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
>>>>>>> refs/remotes/origin/pre_release
    }

    public String getPassword() {
<<<<<<< HEAD
        System.out.print(SysStrings.INPUT_USERNAME);
        return client.getInput();
=======
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
>>>>>>> refs/remotes/origin/pre_release
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

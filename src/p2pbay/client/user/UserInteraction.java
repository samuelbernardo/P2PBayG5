package p2pbay.client.user;

import p2pbay.client.Client;
import p2pbay.client.SysStrings;
import java.io.Console;

/**
 * Classe com metodos necessarios para receber inputs do utilizador
 */
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
            // creates a console object
            Console cnsl = System.console();
            // if console is not null
            if (cnsl != null) {
                username = cnsl.readLine(SysStrings.INPUT_USERNAME);
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return username;
    }

    public String getPassword() {
        String password = null;
        try {
            // creates a console object
            Console cnsl = System.console();
            // if console is not null
            if (cnsl != null) {
                password = new String(cnsl.readPassword(SysStrings.INPUT_PASSWORD));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return password;
    }
    
    public String readInput() {
        return client.readInput();
    }

    public String readInput(String message) {
        System.out.print(message);
        return client.readInput();
    }

    public float readNumberInput(String message) {
        return Float.parseFloat(readInput(message));
    }

    public float getPositiveNumber(String message) {
        while (true) {
            try {
                float value = readNumberInput(message);

                if (value > 0)
                    return value;
            } catch (NumberFormatException ignored) {
                System.out.println(SysStrings.INVALID_VALUE);
            }
        }
    }

    public abstract void getInfo();

    public abstract void doOperation();

    @Override
    public void run() {
        getInfo();
        doOperation();
    }
}

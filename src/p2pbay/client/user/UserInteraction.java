package p2pbay.client.user;

import p2pbay.client.Client;
import p2pbay.client.SysStrings;

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
            username = readInput(SysStrings.INPUT_USERNAME);
        } catch(Exception e){
            e.printStackTrace();
        }
        return username;
    }

    public String getPassword() {
        String password = null;
        try {
            password = getClient().readInputPassword();
        } catch(Exception e){
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

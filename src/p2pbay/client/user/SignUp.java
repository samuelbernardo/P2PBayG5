package p2pbay.client.user;

import p2pbay.client.Client;
import p2pbay.core.User;

import java.io.IOException;
import java.util.Scanner;

public class SignUp extends UserInfo implements Runnable {
    private Client client;

    public SignUp(Client client, Scanner input) {
        super(input);
        this.client = client;
    }

    @Override
    public void run() {
        System.out.println("Sign up...");

        String username = getUsername();
        String password = getPassword();

        //Check if user exists
        User user = client.findUser(username);

        if(user == null) {
            user = new User(username, password);
            client.store(user);
            System.out.println("User registado");
        }
        else
            System.out.println("User não registado");
    }
}
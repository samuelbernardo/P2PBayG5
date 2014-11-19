package p2pbay.client.user;

import p2pbay.client.Client;
import p2pbay.core.User;

import java.util.Scanner;

public class Login extends UserInfo implements Runnable {
    private Client client;

    public Login(Client client, Scanner input) {
        super(input);
        this.client = client;
    }

    @Override
    public void run() {
        System.out.println("Login...");

        String username = getUsername();
        String password = getPassword();

        //Check if exists user with such username
        User user = client.findUser(username);
        if(user != null)
            if(user.getPassword().equals(password)) {
                //User password matches
                client.setUser(user);
                System.out.println("Login com sucesso");
                return;
            }
        System.out.println("Login falhou");
    }
}
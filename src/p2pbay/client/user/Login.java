package p2pbay.client.user;

import p2pbay.client.Client;
import p2pbay.core.User;

import java.util.Scanner;

public class Login extends UserInfo implements Runnable {

    public Login(Client client) {
        super(client);
    }
    @Override
    public void run() {
        System.out.println("Login...");

        String username = getUsername();
        String password = getPassword();

        //Check if exists user with such username
        User user = getClient().findUser(username);
        if(user != null)
            if(user.getPassword().equals(password)) {
                //User password matches
                getClient().setUser(user);
                System.out.println("Login com sucesso");
                return;
            }
        System.out.println("Login falhou");
    }
}
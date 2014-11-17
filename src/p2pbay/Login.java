package p2pbay;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import core.Bid;
import core.User;
import p2pbay.TomP2P;

public class Login {

    private static String username;
    private static String givenPassword;

    public String getUsername() {
        return username;
    }

    public User doLogin(TomP2P tomp2p, Scanner in) throws ClassNotFoundException, IOException {
        setCredentials(in);
        User user = null;
        String storedPassword = tomp2p.get(username, "pass").toString();
        if(givenPassword.equals(storedPassword)) {
            @SuppressWarnings("unchecked")
            List<Bid> bids = (List<Bid>) tomp2p.get(username, "bids");
            user = new User(username, givenPassword, bids);
        }
        return user;
    }

    public User createUser(TomP2P tomp2p, Scanner in) {
        setCredentials(in);
        User user = new User(username, givenPassword);
        try {
            tomp2p.storeUser(user);
            System.out.println("\nA conta foi criada com sucesso!");
        } catch (IOException e) {
            System.out.println("Ocorreu um erro na criacao da conta...\n");
            System.out.println(e.getMessage());
        }
        return user;
    }

    public void setCredentials(Scanner in) {
        System.out.println("\nUsername:");
        username = in.nextLine();
        System.out.println("Password:");
        givenPassword = in.nextLine();
    }
}
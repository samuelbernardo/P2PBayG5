package p2pbay.client;

import java.io.IOException;
import java.util.Scanner;

import p2pbay.core.User;
import p2pbay.server.TomP2PHandler;

public class Login {

    private static String username;
    private static String givenPassword;

    public String getUsername() {
        return username;
    }

    public void setCredentials(Scanner in) {
        System.out.println("\nUsername:");
        username = in.nextLine();
        System.out.println("Password:");
        givenPassword = in.nextLine();
    }
    
    // Por questoes de segurança nao se especifica porque falhou o login.
    public User doLogin(TomP2PHandler tomp2p, Scanner in) throws ClassNotFoundException, IOException {
        setCredentials(in);
        User user = null;
        try {
            user = (User) tomp2p.get(username);
            if(!givenPassword.equals(user.getPassword()))
                System.out.println("O login falhou...");
        }
        catch(ClassNotFoundException | IOException e){
            System.out.println("O login falhou...");
        }
        return user;
    }

    public User createUser(TomP2PHandler tomp2p, Scanner in) {
        setCredentials(in);
        User user = new User(username, givenPassword);
        try {
            tomp2p.store(user.getUsername(), user);
            System.out.println("\nA conta foi criada com sucesso!");
        } catch (IOException e) {
            System.out.println("Ocorreu um erro na criacao da conta...\n");
            System.out.println(e.getMessage());
        }
        return user;
    }
}
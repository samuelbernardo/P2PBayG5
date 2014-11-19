package p2pbay.client;

import java.io.IOException;
import java.util.Scanner;
import java.io.Console;

import p2pbay.P2PBay;
import p2pbay.core.User;
import p2pbay.server.TomP2PHandler;

public class Login {
    public static String USERNAME = "Username";
    public static String PASSWORD = "Password";

    private String username;
    private String givenPassword;

    public Login(String username, String password) {
        this.username = username;
        this.givenPassword = password;
    }

    public String getUsername() {
        return username;
    }

    public void setCredentials(Scanner in) {
        try {
            // creates a console object
            Console cnsl = System.console();
            // if console is not null
            if (cnsl != null) {
                this.username = cnsl.readLine("Username: ");
                this.givenPassword = new String(cnsl.readPassword("Password: "));
            }
        } catch(Exception e){
            // if any error occurs
            e.printStackTrace();
        }
    }
    
    // Por questoes de segurança nao se especifica porque falhou o login.
    public User doLogin(TomP2PHandler tomp2p, Scanner in) throws IOException {
        setCredentials(in);
        User user = null;
        user = (User) tomp2p.get(username);
        if(!givenPassword.equals(user.getPassword()))
            System.out.println("O login falhou...");
        return user;
    }

    /**
     *
     * @return The user that logged on, or null if wrong credentials provided
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public User doLogin() {
        User user = null;
        user = (User) P2PBay.P2PBAY.get(username);
        if(!user.getPassword().equals(givenPassword))
            return null;
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
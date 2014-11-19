package p2pbay.client;

import p2pbay.core.User;
import p2pbay.server.TomP2PHandler;

import java.io.IOException;
import java.util.Scanner;

public class Client {
    public static User LOGGED = null;

    private TomP2PHandler connectionHandler;
    private Scanner inputReader;

    public Client(TomP2PHandler handler) {
        connectionHandler = handler;
        inputReader = new Scanner(System.in);
    }

    /**
     * Used for a user to login onto the client application
     * @return true if user login is successful, false otherwise
     */
    public boolean login() {
        System.out.println("Login...");

        String username = getUsername();
        String password = getPassword();

        //Check if exists user with such username
        User user = (User) connectionHandler.get(username);
        if(user != null)
            if(user.getPassword().equals(password))
                //User password matches
                LOGGED = user;

        return isLogged();
    }

    private boolean signup() {
        System.out.println("Sign up...");

        String username = getUsername();
        String password = getPassword();

        //Check if user exists
        User user = (User) connectionHandler.get(username);

        if(user == null) {
            user = new User(username, password);
            try {
                connectionHandler.store(user.getUsername(), user);
            } catch (IOException e) {
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean isLogged() {
        return LOGGED != null;
    }

    private String getUsername() {
        System.out.print(Login.USERNAME + ":");
        return inputReader.nextLine();
    }

    private String getPassword() {
        System.out.print(Login.PASSWORD + ":");
        return inputReader.nextLine();
    }

    public void start() throws IOException, ClassNotFoundException {
        Scanner in = new Scanner(System.in);
        Menu menu = new Menu(in);
        String option;

        while (!isLogged()) {
            option = menu.showLoginMenu();
            switch (option) {
                case "1":
                    if(login())
                        System.out.println("Logged in as " + LOGGED.getUsername());
                    else
                        System.out.println("Login failed");
                    break;
                case "2":
                    if(signup())
                        System.out.println("Sign up Successful");
                    else
                        System.out.println("Error Signing up");
                    break;
                case "exit":
                    in.close();
                    System.exit(0);
                default:
                    System.out.println("Opcao invalida!");
            }
        }

        menu.navigate(connectionHandler, LOGGED);
    }
}

package p2pbay.client;

import p2pbay.core.User;
import p2pbay.server.TomP2PHandler;

import java.io.IOException;
import java.util.Scanner;

public class Client {
    private static User LOGGEDUSER;
    private static TomP2PHandler CONNECTIONHANDLER;
    private Scanner inputReader;

    public Client(TomP2PHandler handler, Scanner inputReader) {
        LOGGEDUSER = null;
        CONNECTIONHANDLER = handler;
        this.inputReader = inputReader;
    }

    /**
     * Used for a user to login onto the client application
     * @return true if user login is successful, false otherwise
     */
    public boolean login() {
        System.out.println("Login");

        String username = getUsername();
        String password = getPassword();

        User user = (User) CONNECTIONHANDLER.get(username);
        if(user != null) {
            if(user.getPassword().equals(password)) {
                LOGGEDUSER = user;
                return true;
            }
        }
        return false;
    }

    private String getUsername() {
        System.out.print("Username:");
        return inputReader.nextLine();
    }

    private String getPassword() {
        System.out.print("Password:");
        return inputReader.nextLine();
    }

    private boolean signup() {
        System.out.println("Sign up");
        String username = getUsername();
        String password = getPassword();
        User user = (User) CONNECTIONHANDLER.get(username);

        if(user == null) {
            user = new User(username, password);
            try {
                CONNECTIONHANDLER.store(user.getUsername(), user);
                return true;
            } catch (IOException e) {
                return false;
            }
        }
        else
            return false;
    }

    public void start() throws IOException, ClassNotFoundException {
        Menu menu = new Menu(this.inputReader);
        String option;

        while (true) {
            option = menu.showLoginMenu();
            switch (option) {
                case "1":
                    if(login()) {
                        System.out.println("\nLogged in as " + LOGGEDUSER.getUsername());
                        menu.navigate(CONNECTIONHANDLER, LOGGEDUSER);
                    }
                    else
                        System.err.println("\nERROR! The login failed");
                    break;
                case "2":
                    if(signup())
                        System.out.println("\nSign up successful");
                    else
                        System.err.println("\nERROR! The given username already exists");
                    break;
                case "exit":
                    inputReader.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid option");
            }
        }
    }
}

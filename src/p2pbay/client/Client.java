package p2pbay.client;

import p2pbay.client.user.Login;
import p2pbay.client.user.SignUp;
import p2pbay.core.User;
import p2pbay.server.TomP2PHandler;

import java.io.IOException;
import java.util.Scanner;

public class Client {
    public static User LOGGED = null;
    public static Scanner input = new Scanner(System.in);

    private TomP2PHandler connectionHandler;
    private Scanner inputReader;

    public Client(TomP2PHandler handler) {
        connectionHandler = handler;
        inputReader = new Scanner(System.in);
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

    /**
     * @param username
     * @return User or null if not found
     */
    public User findUser(String username) {
        return (User) connectionHandler.get(username);
    }

    public void setUser(User user) {
        LOGGED = user;
    }

    public void store(User user) {
        try {
            connectionHandler.store(user.getUsername(), user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() throws IOException, ClassNotFoundException {
        Scanner in = new Scanner(System.in);
        Menu menu = new Menu(this);
        String option;

        while (!isLogged()) {
            option = menu.showLoginMenu();
            switch (option) {
                case "1":
                    new Login(this, in).run();
                    break;
                case "2":
                    new SignUp(this, in).run();
                    break;
                case "exit":
                    in.close();
                    System.exit(0);
                default:
                    System.out.println("Opcao invalida!");
            }
        }

        menu.navigate();
    }
}

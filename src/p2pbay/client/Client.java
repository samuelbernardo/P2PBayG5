package p2pbay.client;

import p2pbay.client.user.Login;
import p2pbay.client.user.SignUp;
import p2pbay.core.DHTObject;
import p2pbay.core.Item;
import p2pbay.core.User;
import p2pbay.server.TomP2PHandler;

import java.io.IOException;
import java.util.Scanner;

public class Client {
    public static User LOGGED = null;

    private Scanner input = new Scanner(System.in);
    private TomP2PHandler connectionHandler;

    public Client(TomP2PHandler handler) {
        connectionHandler = handler;
    }


    public boolean isLogged() {
        return LOGGED != null;
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

    public boolean store(DHTObject object) {
        return connectionHandler.store(object);
    }

    public String getInput() {
        return input.nextLine();
    }

    public float getNumberInput() {
        return Float.parseFloat(input.nextLine());
    }

    public User getUser() {
        return LOGGED;
    }


    public void start() throws IOException, ClassNotFoundException {
        Menu menu = new Menu(this);
        String option;

        while (!isLogged()) {
            option = menu.showLoginMenu();
            switch (option) {
                case "1":
                    new Login(this).run();
                    break;
                case "2":
                    new SignUp(this).run();
                    break;
                case "exit":
                    close();
                    return;
                default:
                    System.out.println("Opcao invalida!");
            }
        }

        menu.navigate();
    }

    public void close() {
        input.close();
        connectionHandler.close();
    }
}

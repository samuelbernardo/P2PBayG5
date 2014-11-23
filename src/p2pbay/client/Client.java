package p2pbay.client;

import p2pbay.client.commands.Menu;
import p2pbay.client.user.Login;
import p2pbay.client.user.SignUp;
import p2pbay.core.DHTObject;
import p2pbay.core.Index;
import p2pbay.core.Item;
import p2pbay.core.User;
import p2pbay.server.TomP2PHandler;

import java.io.IOException;
import java.util.Scanner;

public class Client {
    public User LOGGED = null;

    private Scanner input = new Scanner(System.in);
    private TomP2PHandler connectionHandler;

    public Client(TomP2PHandler handler) {
        connectionHandler = handler;
    }


    public boolean isLogged() {
        return LOGGED != null;
    }

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

    /**
     * Gets an user from the DHT
     * @param username username of the user
     * @return User or null of not found
     */
    public User getUser(String username) {
        Object item = connectionHandler.get(username);
        if(item != null && item instanceof User) {
            return (User)item;
        }
        return null;    }


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
        close();
    }

    public void close() {
        input.close();
        connectionHandler.close();
    }


    /**
     * Gets an item from the DHT
     * @param title title of the item
     * @return Item or null of not found
     */
    public Item getItem(String title) {
        Object item = connectionHandler.get(title);
        if(item != null && item instanceof Item) {
            return (Item)item;
        }
        return null;
    }

    /**
     * Gets an Index from the DHT
     * @param term term of the Index
     * @return Index or null of not found
     */
    public Index getIndex(String term) {
        Object index = connectionHandler.get(Index.PREFIX + term);
        if(index != null && index instanceof Index) {
            return (Index)index;
        }
        return null;
    }

    public void logout() {
        LOGGED = null;
    }
}

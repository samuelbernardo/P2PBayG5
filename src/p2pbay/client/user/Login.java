package p2pbay.client.user;

import p2pbay.client.Client;
import p2pbay.client.Menu;
import p2pbay.core.User;

public class Login extends UserInteraction implements Runnable {

    public Login(Client client) {
        super(client);
    }

    @Override
    public void getInfo() {
        System.out.println("Login...");

        //User Credentials
        String username = getUsername();
        String password = getPassword();

        //Check if exists user with such username
        User user = getClient().findUser(username);
        if (user != null)
            if (user.getPassword().equals(password)) {
                //User password matches
                getClient().setUser(user);
                System.out.println("Login com sucesso");

                Menu menu = new Menu(getClient());
                menu.navigate();
                return;
            }
        System.out.println("Login falhou");
    }

    @Override
    public void storeObjects() {}
}
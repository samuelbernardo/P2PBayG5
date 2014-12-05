package p2pbay.client.user;

import p2pbay.client.Client;
import p2pbay.client.Menu;
import p2pbay.client.SysStrings;
import p2pbay.core.User;

/**
 * Comando para efectuar o Login do utilizador
 */
public class Login extends UserInteraction implements Runnable {
    private String username;
    private String password;
    
    public Login(Client client) {
        super(client);
    }

    @Override
    public void getInfo() {
        System.out.println(SysStrings.LOGIN);

        username = getUsername();
        password = getPassword();
    }

    @Override
    public void doOperation() {
        //Check if exists user with then given username
        User user = getClient().getUser(username);
        if (user != null)
            if (user.getPassword().equals(password)) {
                getClient().setUser(user.getUsername());
                System.out.println(SysStrings.LOGIN_SUCCESS);

                Menu menu = new Menu(getClient());
                menu.navigate();
                return;
            }
        System.out.println(SysStrings.LOGIN_FAILED);
    }
}
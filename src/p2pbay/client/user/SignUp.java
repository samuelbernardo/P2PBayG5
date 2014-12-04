package p2pbay.client.user;

import p2pbay.client.Client;
import p2pbay.client.SysStrings;
import p2pbay.core.User;

public class SignUp extends UserInteraction implements Runnable {
    private User user = null;
    private String username;
    private String password;

    public SignUp(Client client) {
        super(client);
    }

    @Override
    public void getInfo() {
        System.out.println(SysStrings.SIGNUP);

        username = getUsername();
        password = getPassword();

        //Check if user exists
        user = getClient().findUser(username);
    }

    @Override
    public void doOperation() {
        if(user == null) {
            user = new User(username, password);
            getClient().store(user);
            System.out.println(SysStrings.USER_REGISTERED);
        }
        else
            System.out.println(SysStrings.USER_NOT_REGISTERED);
    }
}

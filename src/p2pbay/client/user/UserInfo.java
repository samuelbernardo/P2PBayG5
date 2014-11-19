package p2pbay.client.user;

import java.util.Scanner;

public class UserInfo {
    public static String USERNAME = "Username:";
    public static String PASSWORD = "Password:";

    private Scanner input;


    public UserInfo(Scanner input) {
        this.input = input;
    }

    protected String getUsername() {
        System.out.print(USERNAME);
        return input.nextLine();
    }

    protected String getPassword() {
        System.out.print(PASSWORD);
        return input.nextLine();
    }
}

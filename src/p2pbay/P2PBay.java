package p2pbay;

import p2pbay.client.Login;
import p2pbay.client.Menu;
import p2pbay.core.User;
import p2pbay.server.P2PBayBootstrap;
import p2pbay.server.TomP2PHandler;
import java.util.Scanner;

public class P2PBay {

    public static void main(String[] args) throws Exception {
        Scanner in = new Scanner(System.in);
        P2PBayBootstrap bootstrap = new P2PBayBootstrap();
        bootstrap.loadConfig();
        TomP2PHandler tomp2p = new TomP2PHandler(bootstrap);
        Menu menu = new Menu();
        Login login = new Login();
        User user;
        String option;
        
        while (true) {
            option = menu.showLoginMenu(in);
            switch (option) {
                case "1":
                    user = login.doLogin(tomp2p, in);
                    if(user == null) break;
                    else menu.navigate(tomp2p, in, user);
                    break;
                case "2":
                    user = login.createUser(tomp2p, in);
                    break;
                case "exit":
                    in.close();
                    System.exit(0);
                default:
                    System.out.println("Opcao invalida!");
            }
        }
    }
}
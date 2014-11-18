package p2pbay.client;

import java.util.Scanner;

//import p2pbay.core.Item;
import p2pbay.core.User;
import p2pbay.server.TomP2PHandler;

public class Menu {

    public String showLoginMenu(Scanner in) {
        System.out.println("\n"
                + "-----P2PBay-----\n\n"
                + "1 - Login\n"
                + "2 - Criar uma conta\n\n"
                + "'exit' para sair\n");
        String option = in.nextLine();
        return option;
    }

    public String showMainMenu(Scanner in) {
        System.out.println("\n"
                + "1 - Vender um item\n"
                + "2 - Fechar leilao\n"
                + "3 - Procurar um item para comprar\n"
                + "4 - Licitar um item\n"
                + "5 - Consultar os detalhes de um item\n"
                + "6 - Consultar o meu historico\n"
                + "7 - Logout\n\n"
                + "'exit' para sair\n");
        String option = in.nextLine();
        return option;
    }

    public void navigate(TomP2PHandler tomp2p, Scanner in, User user) {
        String option;
        while(true){
            option = showMainMenu(in);
            switch (option) {
                case "1":
                    new ItemForSale(tomp2p, in, user);
                    break;
                case "2":
                    new AuctionToClose(tomp2p, in);
                    break;
                case "3":
                    break;
                case "4":
                    new BidOnItem(tomp2p, in, user);
                    break;
                case "5":
                    new DetailsOfItem(tomp2p, in);
                    break;
                case "6":
                    new HistoryCheck(tomp2p, user);
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
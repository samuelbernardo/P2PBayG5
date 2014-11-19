package p2pbay.client;

import java.util.Scanner;

//import p2pbay.core.Item;
import p2pbay.core.User;
import p2pbay.server.TomP2PHandler;

public class Menu {
    private Client client;
    Scanner input;

    public Menu(Scanner input) {
        this.input = input;
    }

    public Menu(Client client) {
        this.client = client;
    }

    public String showLoginMenu() {
        System.out.println("\n"
                + "-----P2PBay-----\n\n"
                + "1 - Login\n"
                + "2 - Criar uma conta\n\n"
                + "'exit' para sair\n");
        String option = input.nextLine();
        return option;
    }

    public String showMainMenu() {
        System.out.println("\n"
                + "1 - Vender um item\n"
                + "2 - Fechar leilao\n"
                + "3 - Procurar um item para comprar\n"
                + "4 - Licitar um item\n"
                + "5 - Consultar os detalhes de um item\n"
                + "6 - Consultar o meu historico\n"
                + "7 - Logout\n\n"
                + "'exit' para sair\n");
        String option = input.nextLine();
        return option;
    }

    public void navigate(TomP2PHandler tomp2p, User user) {
        String option;
        while(true){
            option = showMainMenu();
            switch (option) {
                case "1":
                    new ItemForSale(tomp2p, input, user);
                    break;
                case "2":
                    new AuctionToClose(tomp2p, input);
                    break;
                case "3":
                    break;
                case "4":
                    new BidOnItem(tomp2p, input, user);
                    break;
                case "5":
                    new DetailsOfItem(tomp2p, input);
                    break;
                case "6":
                    new HistoryCheck(tomp2p, user);
                    break;
                case "exit":
                    input.close();
                    System.exit(0);
                default:
                    System.out.println("Opcao invalida!");
            }
        }
    }

    public void navigate() {
        String option;
        while(true){
            option = showMainMenu();
            switch (option) {
                case "1":
//                    new ItemForSale(tomp2p, input, Client.LOGGED);
                    new ItemForSale().run();
                    break;
                case "2":
//                    new AuctionToClose(tomp2p, input);
                    break;
                case "3":
                    break;
                case "4":
//                    new BidOnItem(tomp2p, input, client.LOGGED);
                    break;
                case "5":
//                    new DetailsOfItem(tomp2p, input);
                    break;
                case "6":
//                    new HistoryCheck(tomp2p, client.LOGGED);
                    break;
                case "exit":
                    input.close();
                    System.exit(0);
                default:
                    System.out.println("Opcao invalida!");
            }
        }
    }
}
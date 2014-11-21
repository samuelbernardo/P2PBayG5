package p2pbay.client;

import java.util.Scanner;

//import p2pbay.core.Item;
import p2pbay.core.User;
import p2pbay.server.TomP2PHandler;

public class Menu {
    Scanner input;

    public Menu(Scanner input) {
        this.input = input;
    }

    public String showLoginMenu() {
        System.out.println("\n"
                + "-- P2PBay --\n\n"
                + "1 - Login\n"
                + "2 - Sign up\n\n"
                + "'exit' para sair\n");
        String option = input.nextLine();
        return option;
    }

    public String showMainMenu() {
        System.out.println("\n"
                + "-------------- P2PBay --------------\n\n"
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
                    ItemForSale newItem = new ItemForSale(tomp2p, input, user);
                    newItem.execute();
                    break;
                case "2":
                    AuctionToClose newClose = new AuctionToClose(tomp2p, input, user);
                    newClose.execute();
                    break;
                case "3":
                    Search newSearch = new Search(tomp2p, input);
                    newSearch.execute();
                    break;
                case "4":
                    BidOnItem newBid = new BidOnItem(tomp2p, input, user);
                    newBid.execute();
                    break;
                case "5":
                    DetailsOfItem newDetails = new DetailsOfItem(tomp2p, input);
                    newDetails.execute();
                    break;
                case "6":
                    HistoryCheck newHistory = new HistoryCheck(tomp2p, user);
                    newHistory.execute();
                    break;
                case "7":
                    return;
                case "exit":
                    input.close();
                    System.exit(0);
                default:
                    System.out.println("Opcao invalida!");
            }
        }
    }
}
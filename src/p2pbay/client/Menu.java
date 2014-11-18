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
       /* String title;
        String description;
        Item item;
        float value;
        String result;*/
        String option;

        option = showMainMenu(in);
        switch (option) {
            case "1":
                /*System.out.println("Titulo do item:");
                title = in.nextLine();
                System.out.println("Descricao do item:");
                description = in.nextLine();
                item = new Item(login.getUsername(), title, description);*/
                new ItemForSale(tomp2p);
                break;
            case "2":
                /*System.out.println("Titulo do item:");
                title = in.nextLine();
                result = closeAuction(title);
                System.out.println(result);*/
                new AuctionToClose(tomp2p);
                break;
            case "3":
                break;
            case "4":
                /*System.out.println("Titulo do item:");
                title = in.nextLine();
                System.out.println("Valor:");
                value = Integer.parseInt(in.nextLine());
                result = bidOnItem(title, value);
                System.out.println(result);*/
                new BidOnItem(tomp2p, user);
                break;
            case "5":
                /*System.out.println("Titulo do item:");
                title = in.nextLine();
                result = getDetails(title);
                System.out.println(result);*/
                new DetailsOfItem(tomp2p);
                break;
            case "6":
                /*result = getHistory();
                System.out.println(result);*/
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
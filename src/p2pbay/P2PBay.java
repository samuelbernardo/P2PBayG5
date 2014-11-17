package p2pbay;

import p2pbay.client.Login;
import p2pbay.core.Bid;
import p2pbay.core.Item;
import p2pbay.core.User;
import p2pbay.server.TomP2PHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class P2PBay {
    static Scanner in = new Scanner(System.in);
    static TomP2PHandler tomp2p;
    static Login login;
    static User user;

    public P2PBay() throws Exception {
    }

    public static void main(String[] args) throws Exception {
        String option;
        tomp2p = new TomP2PHandler();
        login = new Login();
        while (true) {
            option=showMenu();
            if(!(option.equals("exit"))) {
                switch (option) {
                    case "1":
                        user = login.doLogin(tomp2p, in);
                        if(user != null) {
                            while(true) {
                                if(!(option = showMainMenu()).equals("7")) {
                                    doOperation(tomp2p, option);
                                }
                                else break;

                            }

                        }
                        else
                            System.out.println("\nO login falhou!");
                        break;
                    case "2":
                        user = login.createUser(tomp2p, in);
                        break;
                    default:
                        System.out.println("Opcao invalida!");
                }
            }
            else {
                in.close();
                System.exit(0);
            }
        }
    }

    private static String showMenu() {
        System.out.println("\n"
                + "-----P2PBay-----\n\n"
                + "1 - Login\n"
                + "2 - Criar uma conta\n\n"
                + "'exit' para sair\n");
        String option = in.nextLine();
        return option;
    }

    public static String showMainMenu() {
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

    public static void doOperation(TomP2PHandler tomp2p, String option) {
        String title;
        String description;
        Item item;
        float value;
        String result;

        if(!(option.equals("exit"))) {
            switch (option) {
                case "1":
                    System.out.println("Titulo do item:");
                    title = in.nextLine();
                    System.out.println("Descricao do item:");
                    description = in.nextLine();
                    item = new Item(login.getUsername(), title, description);
                    sellItem(tomp2p, item);
                    break;
                case "2":
                    System.out.println("Titulo do item:");
                    title = in.nextLine();
                    result = closeAuction(title);
                    System.out.println(result);
                    break;
                case "3":
                    break;
                case "4":
                    System.out.println("Titulo do item:");
                    title = in.nextLine();
                    System.out.println("Valor:");
                    value = Integer.parseInt(in.nextLine());
                    result = bidOnItem(title, value);
                    System.out.println(result);
                    break;
                case "5":
                    System.out.println("Titulo do item:");
                    title = in.nextLine();
                    result = getDetails(title);
                    System.out.println(result);
                    break;
                case "6":
                    result = getHistory();
                    System.out.println(result);
                    break;
                default:
                    System.out.println("Opcao invalida!");
            }
        }
        else {
            in.close();
            System.exit(0);
        }
    }

    public static void sellItem(TomP2PHandler tomp2p, Item item) {
        try {
            tomp2p.storeItem(item);
        } catch (IOException e) {
            System.out.println("Ocorreu um erro na insercao do item no sistema...\n");
        }
        System.out.println("\nO item foi inserido com sucesso!");
    }

    private static void storeItem(Item item) {
    }

    @SuppressWarnings("unchecked")
    public static String closeAuction(String title) {
        boolean isClosed = false;
        List<Bid> bids = new ArrayList<Bid>();
        float value = 0;
        try {
            isClosed = (boolean) tomp2p.get(title, "auctionStatus");
            bids = (List<Bid>) tomp2p.get(title, "bids");
            value = bids.get(bids.size()-1).getValue();
            if (!isClosed)
                tomp2p.closeAuction(title);
            else
                System.out.println("O leilao ja foi fechado anteriormente.");
        }catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return "O leilao foi fechado com sucesso, o valor final do item e " + value;
    }

    @SuppressWarnings("unchecked")
    private static String bidOnItem(String title, float value) {
        List<Bid> bids = new ArrayList<Bid>();
        String result = null;
        try {
            bids = (List<Bid>) tomp2p.get(title, "bids");

            int nBids = bids.size();
            if (nBids==0 || value > (bids.get(nBids-1).getValue())) {
                Bid bid = new Bid(title, user.getUsername(), value);
                bids.add(bid);
                try {
                    tomp2p.storeNewBid(title, "bids", bids);
                    tomp2p.storeNewBid(user.getUsername(), "bids", bids);
                    user.getBids().add(bid);
                    result = "A licitacao foi aceite!";
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else
                result = "A licitacao foi rejeitada pois o valor do item e superior a sua oferta!";

        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public static String getDetails(String title) {
        String description = null;
        float value = 0;
        List<Bid> bids = new ArrayList<Bid>();

        try {
            description = tomp2p.get(title, "description").toString();
            bids = (List<Bid>) tomp2p.get(title, "bids");
            int nBids = bids.size();
            if (nBids != 0)
                value = bids.get(nBids-1).getValue();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return "Descricao: " + description + "\nValor: " + value;
    }

    public static String getHistory() {
        List<Bid> bids = user.getBids();
        String history = "Titulo:\t\tValor:\n";
        for(Bid b : bids) {
            history += b.getTitle() + "\t\t" + b.getValue() + "\n";
        }
        return history;
    }

}

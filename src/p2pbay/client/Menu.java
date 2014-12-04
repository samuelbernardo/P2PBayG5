package p2pbay.client;

//import p2pbay.core.Item;


public class Menu {
    private Client client;

    public Menu(Client client) {
        this.client = client;
    }

    public String showLoginMenu() {
        System.out.println("\n"
                + "-- P2PBay --\n\n"
                + "1 - Login\n"
                + "2 - Sign up\n\n"
                + "'exit' para sair\n");
        String option = client.readInput();
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
        String option = client.readInput();
        return option;
    }

    public void navigate() {
        String option;
        while(true){
            option = showMainMenu();
            switch (option) {
                case "1":
                    new ItemForSale(client).run();
                    break;
                case "2":
                    new AuctionToClose(client).run();
                    break;
                case "3":
                    new Search(client).run();
                    break;
                case "4":
                    new BidOnItem(client).run();
                    break;
                case "5":
                    new DetailsOfItem(client).run();
                    break;
                case "6":
                    new HistoryCheck(client).run();
                    break;
                case "7":
                    client.logout();
                    return;
                case "exit":
                    return;
                default:
                    System.out.println("Opcao invalida!");
            }
        }
    }
}
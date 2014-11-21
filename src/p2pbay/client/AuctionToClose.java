package p2pbay.client;

import p2pbay.client.user.UserInteraction;
import p2pbay.core.Item;

public class AuctionToClose extends UserInteraction{
    private String title;

    public AuctionToClose(Client client) {
        super(client);
    }

    @Override
    public void getInfo() {
        System.out.println("\nTitulo:");
        title = getInput();
    }

    @Override
    public void storeObjects() {
        Item item = getClient().getItem(title);
        if(item == null) {
            System.out.println("Item nao existe!");
            return;
        }
        if(item.auctionIsClosed()) {
            System.out.println("O leilao ja esta fechado...");
        }
        else {
            item.setAuctionClosed(true);
            float value = item.getValue();
            if(getClient().store(item))
                System.out.println("O leilao foi fechado com sucesso, o valor final do item e " + value + "€.");
            else {
                System.out.println("Ocorreu um erro ao actualizar o item...");
            }
        }
    }
}
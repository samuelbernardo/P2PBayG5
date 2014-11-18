package p2pbay.client;

import java.io.IOException;
import java.util.Scanner;

import p2pbay.core.Item;
import p2pbay.server.TomP2PHandler;

public class AuctionToClose {
    private String title;
    private TomP2PHandler tomp2p;

    public AuctionToClose(TomP2PHandler tomp2p, Scanner in) {
        this.tomp2p = tomp2p;
        setInfo(in);
        closeAuction();
    }

    private void setInfo(Scanner in) {
        System.out.println("\nTitulo:");
        title = in.nextLine();
    }

    private void closeAuction() {
        Item item = null;
        try {
            item = (Item) this.tomp2p.get(title);
        } catch (ClassNotFoundException | IOException e) {
            System.out.println("Ocorreu um erro na obtencao do item...");
        }
        if(item.auctionIsClosed()) {
            System.out.println("O leilao ja estava fechado...");
        }
        else {
            item.setAuctionClosed(true);
            float value = item.getValue();
            try {
                tomp2p.store(title, item);
            } catch (IOException e) {
                System.out.println("Ocorreu um erro ao actualizar o item...");
            }
            System.out.println("O leilao foi fechado com sucesso, o valor final do item e " + value + "€.");
        }
    }
}
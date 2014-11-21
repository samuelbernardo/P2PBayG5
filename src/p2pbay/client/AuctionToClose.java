package p2pbay.client;

import java.io.IOException;
import java.util.Scanner;

import p2pbay.core.Index;
import p2pbay.core.Item;
import p2pbay.server.TomP2PHandler;

public class AuctionToClose {
    private String title;
    private TomP2PHandler tomp2p;

    public AuctionToClose(TomP2PHandler tomp2p, Scanner in) {
        this.tomp2p = tomp2p;
        setInfo(in);
        closeAuction();
        removeTitle();
    }

    private void setInfo(Scanner in) {
        System.out.println("\nTitulo:");
        title = in.nextLine();
    }

    private void closeAuction() {
        Item item = null;
        item = (Item) this.tomp2p.get(title);
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
    
    private void removeTitle() {
        for(String term : this.title.split(" ")) {
            removeIndex(term);
        }
    }
    
    public void removeIndex(String term) {
        Index index = null;
        index = (Index) this.tomp2p.get(term);
        if (index != null) {
            index.removeTitle(this.title);
            try {
                tomp2p.store(term, index);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    
    public void printIndex() {
        for(String term : this.title.split(" ")) {
            Index index = (Index) tomp2p.get(term);
            if (index != null)
                System.out.println("Termo: " + index.getTerm() + "Titulo: " + index.getTitles());
        }
    }
}
package p2pbay.client;

import java.io.IOException;
import java.util.Scanner;

import p2pbay.core.Index;
import p2pbay.core.Item;
import p2pbay.core.User;
import p2pbay.server.TomP2PHandler;

public class ItemForSale {
    TomP2PHandler tomp2p;
    private User user;
    private String title;
    private String description;
    private float baseBid;    

    public ItemForSale(TomP2PHandler tomp2p, Scanner input, User user) {
        this.tomp2p = tomp2p;
        this.user = user;
        getInfo(input);
        store();
        indexItem();
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    private void getInfo(Scanner input) {
        System.out.println("Titulo:");
        this.title = input.nextLine();
        System.out.println("Descricao:");
        this.description = input.nextLine();
        this.baseBid = getBaseBid(input);
    }

    private float getBaseBid(Scanner input) {
        float bBid = -1;
        do{
            try {
                System.out.println("Base de licitacao:");
                bBid = Float.parseFloat(input.nextLine());
                if (bBid <= 0) {
                    System.err.println("O valor introduzido nao e valido.");
                }
            }
            catch (NumberFormatException e) {
                System.err.println("O valor introduzido nao e valido.");
            }
        } while (bBid <= 0);
        return bBid;
    }

    private void store() {
        try {
            tomp2p.store(this.title, new Item(this.user.getUsername(), this.title, this.description, this.baseBid));
            System.out.println("O item foi publicado com sucesso!");
        } catch (IOException e) {
            System.err.println("Ocorreu um erro ao publicar o item...");
        }
    }

    public void indexItem() {
        for(String term : this.getTitle().split(" "))
            doIndex(term);
    }

    public void doIndex(String term) {
        String key = "index" + term;
        Index index = (Index) tomp2p.get(key);
        if (index == null)
            index = new Index(key, getTitle());
        else
            index.addTitle(getTitle());
        try {
            tomp2p.store(key, index);
        } catch (IOException e) {
            System.err.println("Ocorreu um erro na insercao do indice...");
        }
    }
}

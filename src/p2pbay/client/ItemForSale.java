package p2pbay.client;

import java.io.IOException;
import java.util.Scanner;

import p2pbay.core.Item;
import p2pbay.core.User;
import p2pbay.server.TomP2PHandler;

public class ItemForSale {
    TomP2PHandler tomp2p;
    private User user;
    private String title;
    private String description;
    private float baseBid;    
    
    public ItemForSale(TomP2PHandler tomp2p, Scanner in, User user) {
        this.tomp2p = tomp2p;
        this.user = user;
        setInfo(in);
        store();
    }
    
    private void setInfo(Scanner in) {
        System.out.println("\nTitulo:");
        this.title = in.nextLine();
        System.out.println("Descricao:");
        this.description = in.nextLine();
        System.out.println("Base de licitacao:");
        this.baseBid = Float.parseFloat(in.nextLine());
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void store() {
        try {
            tomp2p.store(title, new Item(user.getUsername(), this.title, this.description, this.baseBid));
            System.out.println("O item foi publicado com sucesso!");
        } catch (IOException e) {
            System.out.println("Ocorreu um erro ao publicar o item...");
        }
    }
}

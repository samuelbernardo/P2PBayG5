package p2pbay.client;

import java.io.IOException;
import java.util.Scanner;

import p2pbay.core.Bid;
import p2pbay.core.Item;
import p2pbay.core.User;
import p2pbay.server.TomP2PHandler;

public class BidOnItem {
    private TomP2PHandler tomp2p;
    private User user;
    private String title;
    private Float proposedValue;
    private Scanner input;

    public BidOnItem(TomP2PHandler tomp2p, Scanner input, User user) {
        this.tomp2p = tomp2p;
        this.user = user;
        this.input = input;
    }

    public void execute() {
        setInfo();
        bidOnItem();
    }
    
    private void setInfo() {
        System.out.println("Titulo do item:");
        this.title = this.input.nextLine();
        System.out.println("Valor:");
        this.proposedValue = Float.parseFloat(this.input.nextLine());
    }

    private void bidOnItem() {
        Item item = null;
        try {
            item = (Item) this.tomp2p.get(title);
        } catch (ClassCastException e) {
            System.out.println("O item que pretende licitar nao existe...");
            return;
        }
        
        float itemsValue = item.getValue();
        if (proposedValue > itemsValue) {
            Bid newBid =  new Bid(this.title, this.user.getUsername(), this.proposedValue);
            item.addBid(newBid);
            user.addBid(newBid);
            try {
                tomp2p.store(this.title, item);
                tomp2p.store(this.user.getUsername(), this.user);
                System.out.println("A licitacao foi aceite!");
            } catch (IOException e) {
                System.out.println("Ocorreu um erro ao actualizar o item...");
            }
        }
        else
            System.out.println("A licitacao foi rejeitada pois o valor do item (" + itemsValue + "€) e igual ou superior a sua oferta!");
    }
}
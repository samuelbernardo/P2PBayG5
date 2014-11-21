package p2pbay.client;

import java.io.IOException;
import java.util.Scanner;

import p2pbay.core.Bid;
import p2pbay.core.Item;
import p2pbay.core.User;
import p2pbay.server.TomP2PHandler;

public class BidOnItem {
    private TomP2PHandler CONNECTIONHANDLER;
    private User user;
    private String title;
    private Float proposedValue;
    private Scanner inputReader;

    public BidOnItem(TomP2PHandler CONNECTIONHANDLER, Scanner inputReader, User user) {
        this.CONNECTIONHANDLER = CONNECTIONHANDLER;
        this.user = user;
        this.inputReader = inputReader;
    }

    public void execute() {
        setInfo();
        bidOnItem();
    }
    
    private void setInfo() {
        System.out.println("Title:");
        title = inputReader.nextLine();
        proposedValue = getBidValue();
    }

    private float getBidValue() {
        float bidValue = -1;
        do{
            try {
                System.out.println("Bid value:");
                bidValue = Float.parseFloat(inputReader.nextLine());
                if (bidValue <= 0) {
                    System.err.println("This is not a valid value");
                }
            }
            catch (NumberFormatException e) {
                System.err.println("This is not a valid value");
            }
        } while (bidValue <= 0);
        return bidValue;
    }
    
    private void bidOnItem() {
        Item item = null;
        try {
            item = (Item) CONNECTIONHANDLER.get(title);
        } catch (ClassCastException e) {
            System.out.println("The item doesn't exist");
            return;
        }
        
        float itemsValue = item.getValue();
        if (proposedValue > itemsValue) {
            Bid newBid =  new Bid(title, user.getUsername(), proposedValue);
            item.addBid(newBid);
            user.addBid(newBid);
            try {
                CONNECTIONHANDLER.store(title, item);
                CONNECTIONHANDLER.store(user.getUsername(), user);
                System.out.println("The bid was accepted!");
            } catch (IOException e) {
                System.err.println("Error trying to save the bid");
            }
        }
        else
            System.err.println("The bid was rejected because your offer (" + proposedValue + "€) was lower than the item's value");
    }
}
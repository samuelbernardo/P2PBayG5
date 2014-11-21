package p2pbay.client;

import java.io.IOException;
import java.util.Scanner;

import p2pbay.core.Index;
import p2pbay.core.Item;
import p2pbay.core.User;
import p2pbay.server.TomP2PHandler;

public class AuctionToClose {
    private String title;
    private TomP2PHandler CONNECTIONHANDLER;
    private Scanner inputReader;
    private User user;

    public AuctionToClose(TomP2PHandler CONNECTIONHANDLER, Scanner inputReader, User user) {
        this.CONNECTIONHANDLER = CONNECTIONHANDLER;
        this.inputReader = inputReader;
        this.user = user;
    }

    public void execute() {
        getInfo();
        if(isValid()) {
            closeAuction();
            removeTitle();
        }
    }

    private void getInfo() {
        System.out.println("\nTitle:");
        title = inputReader.nextLine();
    }

    private boolean isValid() {
        Item item = (Item) CONNECTIONHANDLER.get(title);
        if (item == null) {
            System.err.println("The item doesn't exist");
            return false;
        }
        if (!item.getOwner().equals(user.getUsername())) {
            System.err.println("You can't close this auction because you're not the owner of the item");
            return false;
        }
        return true;
    }

    private void closeAuction() {
        Item item = null;
        item = (Item) CONNECTIONHANDLER.get(title);
        if(item.auctionIsClosed()) {
            System.err.println("The auction was already closed");
        }
        else {
            item.setAuctionClosed(true);
            float value = item.getValue();
            try {
                CONNECTIONHANDLER.store(title, item);
                System.out.println("The auction was successfuly closed, the final value of the item is " + value + "€.");
            } catch (IOException e) {
                System.err.println("Error removing the item from sale");
            }
        }
    }

    private void removeTitle() {
        for(String term : title.split(" ")) {
            removeIndex(term);
        }
    }

    public void removeIndex(String term) {
        Index index = null;
        String key = "index" + term;
        index = (Index) CONNECTIONHANDLER.get(key);
        if (index != null) {
            index.removeTitle(title);
            try {
                CONNECTIONHANDLER.store(key, index);
            } catch (IOException e) {
                System.err.println("Error updating the index " + term);
            }
        }
    }
}
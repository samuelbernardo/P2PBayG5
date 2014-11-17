package core;

import java.util.ArrayList;
import java.util.List;

public class Item {
    private String title;
    private String description;
    private String owner;
    private boolean auctionClosed;
    private List<Bid> bids;
    
    public Item(String owner, String title, String description) {
        this.title = title;
        this.description = description;
        this.owner = owner;
        this.auctionClosed = false;
        this.bids = new ArrayList<Bid>();
    }
    
    public String getTitle() {
        return this.title;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public String getOwner() {
        return this.owner;
    }
    
    public void setAuctionClosed(boolean bool) {
        this.auctionClosed = bool;
    }
    
    public boolean isAuctionClosed() {
        return this.auctionClosed;
    }
    
    public float getValue() {
        Bid lastBid = bids.get(bids.size()-1);
        return lastBid.getValue();
    }
    
    public List<Bid> getBids() {
        return this.bids;
    }
    
    public void addBid(Bid bid) {
        bids.add(bid);
    }
}

package p2pbay.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Item implements Serializable {
    private static final long serialVersionUID = 6128016096756071380L;
    private String owner;
    private String title;
    private String description;
    private float baseBid;
    private boolean auctionClosed;
    private List<Bid> bids;

    public Item(String owner, String title, String description, float baseBid) {
        this.owner = owner;
        this.title = title;
        this.description = description;
        this.baseBid = baseBid;
        this.auctionClosed = false;
        this.bids = new ArrayList<Bid>();
    }

    public String getOwner() {
        return this.owner;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public float getBaseBid() {
        return this.baseBid;
    }

    public void setAuctionClosed(boolean bool) {
        this.auctionClosed = bool;
    }

    public boolean auctionIsClosed() {
        return this.auctionClosed;
    }

    public float getValue() {
        int nBids = bids.size();
        float value = 0;
        if (nBids==0)
            value = this.baseBid;
        else {
            Bid lastBid = bids.get(bids.size()-1);
            value = lastBid.getValue();
        }
        return value;
    }

    public List<Bid> getBids() {
        return this.bids;
    }

    public void addBid(Bid bid) {
        bids.add(bid);
    }
}

package p2pbay.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Item extends DHTObject implements Serializable {
    private static final long serialVersionUID = 6128016096756071380L;
    private String owner;
    private String title;
    private String description;
    private float baseBid;
    private boolean auctionClosed;

    public Item(String owner, String title, String description, float baseBid) {
        super(title, DHTObjectType.ITEM);
        this.owner = owner;
        this.title = title;
        this.description = description;
        this.baseBid = baseBid;
        this.auctionClosed = false;
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
        return baseBid;
    }

    public String getValueToString() {
        return getValue() + "€";
    }

    @Override
    public String toString() {
        return super.toString() + owner + ":" + title + ":" + description + ":" + getValueToString();
    }
}

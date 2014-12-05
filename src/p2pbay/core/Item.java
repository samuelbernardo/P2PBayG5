package p2pbay.core;

import java.io.Serializable;

public class Item extends DHTObject implements Serializable {
    private static final long serialVersionUID = 6128016096756071380L;
    private String owner;
    private String title;
    private String description;
    private boolean auctionClosed;
    private float value;

    public Item(String owner, String title, String description, float baseBid) {
        super(title, DHTObjectType.ITEM);
        this.owner = owner;
        this.title = title;
        this.description = description;
        this.value = baseBid;
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

    public void setAuctionClosed(boolean bool) {
        this.auctionClosed = bool;
    }

    public boolean auctionIsClosed() {
        return this.auctionClosed;
    }

    public float getValue() {
        return value;
    }
    
    public String getValueToString() {
        return getValue() + "€";
    }

    public void setValue(float value) {
        this.value = value;
    }
    
    @Override
    public String toString() {
        return super.toString() + owner + ":" + title + ":" + description + ":" + getValueToString();
    }
}

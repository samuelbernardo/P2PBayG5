package p2pbay.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User extends DHTObject implements Serializable {
    private static final long serialVersionUID = 6128016096756071380L;
    private String username;
    private String password;
    private List<Bid> bids;
    private List<Item> boughtItems;

    public User(String username, String password) {
        super(username, DHTObjectType.USER);
        this.username = username;
        this.password = password;
        bids = new ArrayList<>();
        boughtItems = new ArrayList<>();
    }
    
    public String getUsername() {
        return this.username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public List<Bid> getBids() {
        return bids;
    }
    
    public List<Item> getBoughtItems() {
        return boughtItems;
    }
    
    public void addBid(Bid bid) {
        this.bids.add(bid);
    }

    public void addItem(Item item) {
        this.boughtItems.add(item);
    }
    
    @Override
    public String toString() {
        return super.toString() + username + ":" + password;
    }
}

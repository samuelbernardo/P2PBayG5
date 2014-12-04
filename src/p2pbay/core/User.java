package p2pbay.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User extends DHTObject implements Serializable {
    private static final long serialVersionUID = 6128016096756071380L;
    private String username;
    private String password;
    private List<Bid> bids;

    public User(String username, String password) {
        super(username, DHTObjectType.USER);
        this.username = username;
        this.password = password;
        this.bids = new ArrayList<Bid>();
    }
    
    public User(String username, String password, List<Bid> bids) {
        super(username, DHTObjectType.USER);
        this.username = username;
        this.password = password;
        this.bids = bids;
    }
    
    public String getUsername() {
        return this.username;
    }
    
    public String getPassword() {
        return this.password;
    }
    
    public List<Bid> getBids() {
        return this.bids;
    }
    
    public void addBid(Bid bid) {
        this.bids.add(bid);
    }

    @Override
    public String toString() {
        return super.toString() + username + ":" + password;
    }
}

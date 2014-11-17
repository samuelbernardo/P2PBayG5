package p2pbay.core;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String username;
    private String password;
    private List<Bid> bids;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.bids = new ArrayList<Bid>();
    }
    
    public User(String username, String password, List<Bid> bids) {
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
}

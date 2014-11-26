package p2pbay.core;

import net.tomp2p.peers.Number160;

import java.io.Serializable;

public class Bid extends DHTObject implements Serializable {
    private static final long serialVersionUID = 6128016096756071380L;
    private String title;
    private float value;
    private String owner;
    
    public Bid(String title, User owner, float value) {
        super(title+owner, DHTObjectType.BID);
        this.title = title;
        this.owner = owner.getUsername();
        this.value = value;
    }
    
    public String getTitle() {
        return this.title;
    }
    
    public float getValue() {
        return this.value;
    }
    
    public String getOwner() {
        return this.owner;
    }
}

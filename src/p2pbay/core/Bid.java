package p2pbay.core;

import net.tomp2p.peers.Number160;

import java.io.Serializable;
import java.util.Comparator;

public class Bid extends DHTObject implements Serializable{
    private static final long serialVersionUID = 6128016096756071380L;
    private String title;
    private float value;
    private String owner;
    private long time;
    
    public Bid(String title, User owner, float value) {
        super(title, DHTObjectType.BID);
        this.title = title;
        this.owner = owner.getUsername();
        this.value = value;
        this.time = 0;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getTime() {
        return time;
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

    @Override
    public String toString() {
        return super.toString() + owner + ":" + value + ":" + time;
    }
}

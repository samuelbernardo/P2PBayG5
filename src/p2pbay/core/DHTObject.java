package p2pbay.core;

import net.tomp2p.peers.Number160;

import java.io.Serializable;

public class DHTObject implements Serializable{
    private String itemKey;
    private Number160 itemKeyHash;
    
    public DHTObject(String key) {
        this.itemKey = key;
        this.itemKeyHash = Number160.createHash(key);
    }
    
    public Number160 getHash() {
        return itemKeyHash;
    }
    
    public String getKey() {
        return itemKey;
    }

    @Override
    public String toString() {
        return "[" + itemKey + "]";
    }
}

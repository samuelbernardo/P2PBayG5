package p2pbay.core;

import net.tomp2p.peers.Number160;

import java.io.Serializable;

public class DHTObject implements Serializable{
    private Number160 itemKeyHash;
    private DHTObjectType type;
    private String key;
    
    public DHTObject(String key, DHTObjectType type) {
        this.itemKeyHash = Number160.createHash(key);
        this.type = type;
        this.key = key;
    }

    public DHTObjectType getType() {
        return type;
    }

    public Number160 getKey() {
        return itemKeyHash;
    }

    public Number160 getContentKey() {
        return type.getContentKey();
    }

    @Override
    public String toString() {
        return "[" + key + "]";
    }
}

package p2pbay.core;

import net.tomp2p.peers.Number160;

import java.io.Serializable;

public class DHTObject implements Serializable{
    private String itemKey;
    private Number160 itemKeyHash;
    private DHTObjectType type;
    
    public DHTObject(String key, DHTObjectType type) {
        this.itemKey = key;
        this.itemKeyHash = type.getKey(key);
        this.type = type;
    }

    public DHTObjectType getType() {
        return type;
    }

    public Number160 getKey() {
        return itemKeyHash;
    }
}

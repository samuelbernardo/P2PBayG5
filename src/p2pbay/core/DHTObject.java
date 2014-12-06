package p2pbay.core;

import net.tomp2p.peers.Number160;

import java.io.Serializable;

public class DHTObject implements Serializable{
    private static final long serialVersionUID = 1L;
    private String key;
    private Number160 keyHash;
    private DHTObjectType type;
    
    public DHTObject(String key, DHTObjectType type) {
        this.keyHash = Number160.createHash(key);
        this.type = type;
        this.key = key;
    }

    public DHTObjectType getType() {
        return type;
    }

    public Number160 getKey() {
        return keyHash;
    }

    public Number160 getContentKey() {
        return type.getContentKey();
    }

    @Override
    public String toString() {
        return "[" + key + "]";
    }
}

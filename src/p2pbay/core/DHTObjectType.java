package p2pbay.core;

import net.tomp2p.peers.Number160;

public enum DHTObjectType {
    BID, INDEX, ITEM, USER;

    @Override
    public String toString() {
        switch (this) {
            case BID: return "BID";
            case INDEX: return "INDEX";
            case ITEM: return "ITEM";
            case USER: return "USER";
        }
        return super.toString();
    }

    public Number160 getContentKey() {
        switch (this) {
            case BID: return Number160.createHash("BID");
            case INDEX: return Number160.createHash("INDEX");
            case ITEM: return Number160.createHash("ITEM");
            case USER: return Number160.createHash("USER");
        }
        return null; //Nunca chega aqui!!!
    }
}

package p2pbay.core;

import net.tomp2p.peers.Number160;

public enum DHTObjectType {
    BID, INDEX, ITEM, USER;

    private static final Number160 bid = Number160.createHash("BID");
    private static final Number160 index = Number160.createHash("INDEX");
    private static final Number160 item = Number160.createHash("ITEM");
    private static final Number160 user = Number160.createHash("USER");

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
            case BID: return bid;
            case INDEX: return index;
            case ITEM: return item;
            case USER: return user;
        }
        return null; //Nunca chega aqui!!!
    }
}

package p2pbay.server;

import net.tomp2p.peers.Number160;
import net.tomp2p.storage.Data;
import net.tomp2p.storage.StorageMemory;
import p2pbay.core.DHTObject;
import p2pbay.core.DHTObjectType;

import java.io.IOException;

public class BayStorage extends StorageMemory {
    private int id;
    private TomP2PHandler handler;

    public BayStorage(int id, TomP2PHandler handler) {
        this.id = id;
        this.handler = handler;
    }

    @Override
    public boolean put(Number160 locationKey, Number160 domainKey, Number160 contentKey, Data value) {
        try {
//            System.out.println(id + ":Storing:" + value.getObject() + ":" + locationKey.shortValue() + ":"
//                                  + domainKey.shortValue() + ":" + contentKey.shortValue());

            handler.addInfo((DHTObject)value.getObject());
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }

        return super.put(locationKey, domainKey, contentKey, value);
    }

    @Override
    public Data get(Number160 locationKey, Number160 domainKey, Number160 contentKey) {
//        System.out.println(id + ":Reading:" + locationKey.shortValue() + ":" + domainKey.shortValue() + ":" + contentKey.shortValue());
        return super.get(locationKey, domainKey, contentKey);
    }
}

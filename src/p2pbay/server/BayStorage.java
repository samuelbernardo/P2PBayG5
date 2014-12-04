package p2pbay.server;

import net.tomp2p.peers.Number160;
import net.tomp2p.storage.Data;
import net.tomp2p.storage.StorageMemory;
import p2pbay.core.Bid;

import java.io.IOException;

public class BayStorage extends StorageMemory {
    @Override
    public boolean put(Number160 locationKey, Number160 domainKey, Number160 contentKey, Data value) {
        Data data = value;
        try {
            if(value.getObject() instanceof Bid) {
                Bid bid = (Bid) value.getObject();
                if (bid.getTime() == 0) {
                    bid.setTime(System.currentTimeMillis());
                    System.out.println("bid = " + bid);
                    data = new Data(bid);
                }
            }
            System.out.println("Access Control: Storing" + locationKey + ":" + domainKey + ":" + contentKey + ":" + data.getObject());
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return super.put(locationKey, domainKey, contentKey, data);
    }

    @Override
    public Data get(Number160 locationKey, Number160 domainKey, Number160 contentKey) {
        System.out.println("Access Control: Reading" + locationKey + ":" + domainKey + ":" + contentKey);
        return super.get(locationKey, domainKey, contentKey);
    }
}

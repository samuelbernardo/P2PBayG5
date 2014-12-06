package p2pbay.testing;

import net.tomp2p.peers.Number160;

import java.util.HashMap;

public class IDCount {
    HashMap<Number160, Integer> hashMap;

    public IDCount() {
        hashMap =  new HashMap<>();
    }

    public void add(Number160 id) {
        synchronized (this) {
            if (hashMap.containsKey(id))
                hashMap.put(id, hashMap.get(id) + 1);
            else
                hashMap.put(id, 1);
        }
    }

    public HashMap<Number160, Integer> getHashMap() {
        return hashMap;
    }
}

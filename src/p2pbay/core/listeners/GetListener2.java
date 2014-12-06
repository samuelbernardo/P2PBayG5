package p2pbay.core.listeners;

import net.tomp2p.futures.BaseFuture;
import net.tomp2p.futures.BaseFutureAdapter;
import net.tomp2p.futures.FutureDHT;
import p2pbay.core.DHTObject;

/**
 * Listener criado para que o cliente consiga realizar varias operacoes de leitura em paralelo
 */
public class GetListener extends BaseFutureAdapter<BaseFuture> {
    private FutureDHT futureDHT;
    private DHTObject object;
    private String key;

    public GetListener(String key) {
        object = null;
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public DHTObject getObject() {
        try {
            futureDHT.awaitListeners();
        } catch (InterruptedException e) {
            System.err.println("Error waiting for listeners");
        }
        return object;
    }

    public void setFutureDHT(FutureDHT futureDHT) {
        this.futureDHT = futureDHT;
    }

    @Override
    public void operationComplete(BaseFuture future) throws Exception {
        if (future.isSuccess())
            object = (DHTObject) ((FutureDHT)future).getData().getObject();
    }
}

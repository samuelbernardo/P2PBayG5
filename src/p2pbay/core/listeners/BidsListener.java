package p2pbay.core.listeners;

import net.tomp2p.futures.BaseFuture;
import net.tomp2p.futures.BaseFutureAdapter;
import net.tomp2p.futures.FutureDHT;
import net.tomp2p.storage.Data;
import p2pbay.core.Bid;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BidsListener extends BaseFutureAdapter<BaseFuture> {
    private FutureDHT futureDHT;
    private List<Bid> bidList;

    public BidsListener(FutureDHT futureDHT) {
        this.futureDHT = futureDHT;
        bidList = new ArrayList<>();
    }

    public void await() {
        futureDHT.awaitUninterruptibly();
        System.out.println("Waited");
        try {
            futureDHT.awaitListeners();
            System.out.println("Listeners");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void operationComplete(BaseFuture future) throws Exception {
        if (futureDHT.isSuccess()) {
            try {
                for (Data map : futureDHT.getDataMap().values()) {
                    if (map.getObject() instanceof  Bid)
                        bidList.add((Bid) map.getObject());
                }
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
        }
    }
}

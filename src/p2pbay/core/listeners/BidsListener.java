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

    public BidsListener() {
        bidList = new ArrayList<>();
    }

    public void setFutureDHT(FutureDHT futureDHT) {
        this.futureDHT = futureDHT;
    }

    public List<Bid> getBidList() {
        try {
            futureDHT.awaitListeners();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return bidList;
    }

    @Override
    public void operationComplete(BaseFuture future) throws Exception {

    }
}

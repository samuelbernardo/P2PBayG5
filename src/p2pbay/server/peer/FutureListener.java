package p2pbay.server.peer;

import net.tomp2p.futures.BaseFuture;
import net.tomp2p.futures.BaseFutureAdapter;

public class FutureListener extends BaseFutureAdapter<BaseFuture> {

    @Override
    public void operationComplete(BaseFuture future) throws Exception {
        System.out.println("future = " + future);
    }
}

package p2pbay.server.peer;

import com.sun.org.apache.xpath.internal.SourceTree;
import net.tomp2p.futures.BaseFuture;
import net.tomp2p.futures.BaseFutureAdapter;
import net.tomp2p.futures.BaseFutureListener;

public class FutureBListener implements BaseFutureListener<BaseFuture> {

    @Override
    public void operationComplete(BaseFuture future) throws Exception {
        System.out.println("future = " + future.getFailedReason());
    }

    @Override
    public void exceptionCaught(Throwable t) throws Exception {
        System.out.println("Caught exception " + t.getMessage());
    }
}

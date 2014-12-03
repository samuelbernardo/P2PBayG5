package p2pbay.server.peer;

import net.tomp2p.peers.PeerAddress;
import net.tomp2p.rpc.ObjectDataReply;

public class CommandReceiver implements ObjectDataReply {

    @Override
    public Object reply(PeerAddress sender, Object request) throws Exception {
        System.out.println("sender = " + sender);
        System.out.println("request = " + request);
        return null;
    }
}

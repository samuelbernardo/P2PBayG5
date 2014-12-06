package p2pbay.server.messages;

import net.tomp2p.peers.PeerAddress;
import net.tomp2p.rpc.ObjectDataReply;
import p2pbay.P2PBay;

public class Receiver implements ObjectDataReply {
    @Override
    public Object reply(PeerAddress sender, Object request) throws Exception {
        if (request instanceof Message) {
            switch ((Message) request) {
                case TEST:
                    System.out.println("Received " + request.getClass());
                    break;
                case SHUTDOWN:
                    System.out.println("Got message from " + sender);
                    P2PBay.P2PBAY.shutdownNetwork();
                    break;
            }
        }
        return null;
    }
}

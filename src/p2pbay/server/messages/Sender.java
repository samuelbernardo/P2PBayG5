package p2pbay.server.messages;

import net.tomp2p.futures.FutureResponse;
import net.tomp2p.p2p.Peer;
import net.tomp2p.peers.PeerAddress;

import java.util.ArrayList;
import java.util.List;

public class Sender {
    Peer peer;
    Message message;

    public Sender(Peer peer, Message message) {
        this.peer = peer;
        this.message = message;
    }

    public void broadcast() {
        List<FutureResponse> futureResponses = new ArrayList<>();
        for (PeerAddress peerAddress : peer.getPeerBean().getPeerMap().getAll()) {
            System.out.println("Sending: " + message + " to " + peerAddress);
            futureResponses.add(peer.sendDirect(peerAddress).setObject(message).start());
        }
        for (FutureResponse response : futureResponses) {
            response.awaitUninterruptibly();
        }
    }
}
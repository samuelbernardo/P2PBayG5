package p2pbay.server.peer;

import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerListener;
import net.tomp2p.peers.PeerAddress;

public class PeerActionListener implements PeerListener {
    private Peer peer;

    public PeerActionListener(Peer peer) {
        this.peer = peer;
    }

    @Override
    public void notifyOnShutdown() {
        System.out.println(getClass() + ":Shutdown " + peer);
    }

    @Override
    public void notifyOnStart() {
        System.out.println(getClass() + ":Start " + peer);
    }

    @Override
    public void serverAddressChanged(PeerAddress peerAddress, PeerAddress reporter, boolean tcp) {
        System.out.println(getClass() + ":Address Changed " + reporter);
    }
}
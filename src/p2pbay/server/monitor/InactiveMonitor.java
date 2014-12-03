package p2pbay.server.monitor;

import net.tomp2p.p2p.Peer;
import net.tomp2p.peers.PeerAddress;
import p2pbay.core.DHTObject;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Collection;

public class InactiveMonitor implements ServerMonitor{
    @Override
    public void print(String message) {

    }

    @Override
    public void printPeer(Peer peer) {

    }

    @Override
    public void printTrying(InetAddress address) {

    }

    @Override
    public void printConnectedTo(Collection<PeerAddress> bootstrapTo) {

    }

    @Override
    public void errorStoring(IOException e, Object object) {

    }

    @Override
    public void printStored(DHTObject object) {

    }
}

package p2pbay.server.monitor;

import net.tomp2p.p2p.Peer;
import net.tomp2p.peers.PeerAddress;
import p2pbay.core.DHTObject;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Collection;

public class ActiveMonitor implements ServerMonitor {
    @Override
    public void print(String message) {
        System.out.println(message);
    }

    @Override
    public void printPeer(Peer peer) {
        System.out.println("peer = " + peer.getPeerAddress());
    }

    @Override
    public void printTrying(InetAddress address) {
        System.out.println("Trying connection to " + address.getHostName());
    }

    @Override
    public void printConnectedTo(Collection<PeerAddress> bootstrapTo) {
        System.out.println("Connected to " + bootstrapTo);
    }

    @Override
    public void errorStoring(IOException e, Object object) {
        System.err.println("Nao foi possivel guardar o objecto:");
        System.err.println(object);
        System.err.println("Expecao " + e);
    }

    @Override
    public void printStored(DHTObject object) {
        System.out.println("Stored " + object);
    }
}

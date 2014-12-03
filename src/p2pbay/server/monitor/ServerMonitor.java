package p2pbay.server.monitor;

import net.tomp2p.p2p.Peer;
import net.tomp2p.peers.PeerAddress;
import p2pbay.core.DHTObject;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Collection;

public interface ServerMonitor {
    void print(String message);

    void printPeer(Peer peer);

    void printTrying(InetAddress address);

    void printConnectedTo(Collection<PeerAddress> bootstrapTo);

    void errorStoring(IOException e, Object object);

    void printStored(DHTObject object);
}

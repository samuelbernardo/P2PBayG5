package p2pbay.server;

import net.tomp2p.p2p.Peer;
import net.tomp2p.peers.PeerAddress;

public class SystemInfothread extends Thread {
    Peer peer;

    public SystemInfothread(Peer peer) {
        this.peer = peer;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException ignore) {}

        while (peer.isRunning()) {
            System.out.println(peer.getPeerBean().getPeerMap().getAll().size());
            for (PeerAddress address : peer.getPeerBean().getPeerMap().getAll()) {
                System.out.println("address = " + address);
            }
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                break;
            }
        }
        System.out.println("Find Peers Existed");
    }
}

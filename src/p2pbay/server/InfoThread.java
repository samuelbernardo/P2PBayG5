package p2pbay.server;

import net.tomp2p.peers.PeerAddress;

import java.util.Random;

public class InfoThread extends Thread {
    private TomP2PHandler handler;
    private Random random;

    public InfoThread(TomP2PHandler tomP2PHandler) {
        this.handler = tomP2PHandler;
        this.random = new Random();
    }

    @Override
    public void run() {
        try {
            Thread.sleep(10000 + random.nextInt(20000));
        } catch (InterruptedException ignore) {}

        while (handler.isRunning()) {
            if (handler.getSystemInfo().isUpdated()) {
                for (PeerAddress address : handler.getNeighbors()) {
//                System.out.println("address = " + address);
                    handler.sendInfo(address);
                }
                handler.getSystemInfo().setUpToDate();
            }

            try {
                Thread.sleep(15000 + random.nextInt(20000));
            } catch (InterruptedException e) {
                break;
            }
        }
        System.out.println("Find Peers Existed");
    }
}

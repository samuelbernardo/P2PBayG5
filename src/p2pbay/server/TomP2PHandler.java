package p2pbay.server;

import net.tomp2p.futures.*;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerMaker;
import net.tomp2p.peers.Number160;
import net.tomp2p.storage.Data;
import p2pbay.core.DHTObject;
import p2pbay.server.monitor.ServerMonitor;
import p2pbay.server.monitor.ServerMonitors;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;

public class TomP2PHandler {
    private Peer peer;
    private int port = 4001;
    private P2PBayBootstrap bootstrap;
    private ServerMonitor monitor;

    public TomP2PHandler(P2PBayBootstrap bootstrap, boolean monitor) {
        this.bootstrap = bootstrap;
        this.monitor = ServerMonitors.getMonitor(monitor);
    }

    public TomP2PHandler(P2PBayBootstrap bootstrap, int port, boolean monitor) {
        this(bootstrap, monitor);
        this.port = port;
    }

    /**
     *
     * @return True if connected to the P2PBay network.
     *          False otherwise.
     */
    public boolean connect() {
        /* Creation of a peer. */
        try {
            peer = new PeerMaker(Number160.createHash(Inet4Address.getLocalHost().getHostAddress())).setPorts(port).makeAndListen();
        } catch (IOException e) {
            System.err.println("Error getting address:" + e.getMessage());
            return false;
        }
        monitor.printPeer(peer);
        // Procura por todos os nos dados pelo objecto P2PBayBoostrap
        for(InetAddress address:bootstrap.getNodes()) {
            monitor.printTrying(address);
            FutureDiscover futureDiscover = peer.discover().setInetAddress(address).setPorts(port).start();
            futureDiscover.awaitUninterruptibly();
            FutureBootstrap fb = peer.bootstrap().setInetAddress(address).setPorts(port).start();
            fb.awaitUninterruptibly();
            if (fb.getBootstrapTo() != null) {
                monitor.printConnectedTo(fb.getBootstrapTo());
                peer.discover().setPeerAddress(fb.getBootstrapTo().iterator().next()).start().awaitUninterruptibly();
                return true;
            }
        }
        return false;
    }

    /**
     * Guarda qualquer objecto na dht
     * @param object Objecto a ser guardado na dht
     * @throws IOException possivelmente se o objecto nao for serializavel
     */
    public boolean store(DHTObject object) {
        try {
            peer.put(object.getHash()).setData(new Data(object)).start().awaitUninterruptibly();
            monitor.printStored(object);
            return true;
        } catch (IOException e) {
            monitor.errorStoring(e,object);
            return false;
        }
    }

    /**
     * Gets an object from the DHT
     * @param key Object Key
     * @return The Object or null if not found
     */
    public Object get(String key) {
        Number160 hKey = Number160.createHash(key);
        FutureDHT futureDHT = peer.get(hKey).start().awaitUninterruptibly();
        if (futureDHT.isSuccess()) {
            try {
                return futureDHT.getData().getObject();
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void close() {
        peer.shutdown();
    }
}

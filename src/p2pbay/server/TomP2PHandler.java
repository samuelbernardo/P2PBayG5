package p2pbay.server;

import net.tomp2p.futures.*;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerListener;
import net.tomp2p.p2p.PeerMaker;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.peers.PeerMap;
import net.tomp2p.rpc.ObjectDataReply;
import net.tomp2p.storage.Data;
import net.tomp2p.task.AsyncTask;
import p2pbay.core.DHTObject;
import p2pbay.core.Item;
import p2pbay.core.User;
import p2pbay.server.monitor.ServerMonitor;
import p2pbay.server.monitor.ServerMonitors;
import p2pbay.server.peer.CommandReceiver;
import p2pbay.server.peer.FutureBListener;
import p2pbay.server.peer.FutureListener;
import p2pbay.server.peer.PeerActionListener;

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
        int peersPort = 4001;
        /* Creation of a peer. */
        try {
            peer = new PeerMaker(Number160.createHash(Inet4Address.getLocalHost().getHostAddress() + port)).setPorts(port).makeAndListen();
        } catch (IOException e) {
            System.err.println("Error getting address:" + e.getMessage());
            return false;
        }



        peer.addPeerListener(new PeerActionListener(peer));

        peer.setObjectDataReply(new CommandReceiver());

        monitor.printPeer(peer);
        // Procura por todos os nos dados pelo objecto P2PBayBoostrap
        for(InetAddress address:bootstrap.getNodes()) {
            monitor.printTrying(address);
            FutureDiscover futureDiscover = peer.discover().setInetAddress(address).setPorts(peersPort).start().addListener(new FutureListener());
            futureDiscover.awaitUninterruptibly();
            FutureBootstrap fb = peer.bootstrap().setInetAddress(address).setPorts(peersPort).start();
            fb.addListener(new FutureBListener());
            fb.awaitUninterruptibly();
            if (fb.getBootstrapTo() != null) {
                monitor.printConnectedTo(fb.getBootstrapTo());
                PeerAddress peerAddress = fb.getBootstrapTo().iterator().next();
                peer.discover().setPeerAddress(peerAddress).start().awaitUninterruptibly();

                peer.sendDirect(peerAddress).setObject(new User("teste", "teste")).start().addListener(new FutureBListener());
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

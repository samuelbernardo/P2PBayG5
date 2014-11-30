package p2pbay.server;

import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.futures.FutureDHT;
import net.tomp2p.futures.FutureDiscover;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerMaker;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.storage.Data;
import net.tomp2p.storage.StorageMemory;
import p2pbay.core.DHTObject;
import p2pbay.core.DHTObjectType;
import p2pbay.server.monitor.ActiveMonitor;
import p2pbay.server.monitor.ServerMonitor;
import p2pbay.server.peer.Node;

import java.io.IOException;
import java.net.Inet4Address;
import java.util.List;
import java.util.Random;

public class TomP2PHandler {
    private Peer peer;
    private int port = 4001;
    private StorageMemory storage;
    private P2PBayBootstrap bootstrap;
    private ServerMonitor monitor;
    final static private String test = "test";

    public TomP2PHandler(P2PBayBootstrap bootstrap) {
        this.bootstrap = bootstrap;
        this.monitor = new ActiveMonitor();
    }

    public boolean connect() throws IOException {
        Random r = new Random();

        PeerAddress localPeerAddress = new PeerAddress(Number160.createHash(Inet4Address.getLocalHost().getHostAddress() + r.nextInt()));

        //** CREATE A NEW PEER **//
        PeerMaker peerMaker = new PeerMaker(localPeerAddress.getID());
        StorageMemory storageMemory = new BayStorage();
        peerMaker.setStorage(storageMemory);

        peerMaker.setPorts(1000 + r.nextInt(10000));
        peerMaker.setEnableIndirectReplication(true);
        System.out.println(peerMaker.isEnableIndirectReplication());
        storage = storageMemory;
        peer = peerMaker.makeAndListen();
        monitor.printPeer(peer);


        // ** Testing lambdas ** //
        // Executed when receiving a direct message.
        peer.setObjectDataReply((sender, request) -> {
            if (request instanceof MessageType) {
                switch ((MessageType) request) {
                    case TEST:
                        System.out.println("Received " + request.getClass());
                        break;
                }
            }
            System.out.println("sender = " + sender);
            System.out.println("request = " + request);
            return "ok";
        });

        new SystemInfoThread(this).start();

        // Procura por todos os nos dados pelo objecto P2PBayBoostrap
        for(Node node:bootstrap.getNodes()) {
            System.out.println("Trying " + node.getHostName() + ":" + node.getPort());
            FutureDiscover futureDiscover = peer.discover().setInetAddress(node.getAddress()).setPorts(node.getPort()).start();
            futureDiscover.awaitUninterruptibly();

            FutureBootstrap fb = peer.bootstrap().setInetAddress(node.getAddress()).setPorts(node.getPort()).start();
            fb.awaitUninterruptibly();
            if (fb.getBootstrapTo() != null) {
                for (PeerAddress peerAddress : fb.getBootstrapTo()) {
                    System.out.println("peerAddress = " + peerAddress);
                }
                System.out.println("Connected to " + fb.getBootstrapTo());
                PeerAddress peerAddress = fb.getBootstrapTo().iterator().next();
                peer.discover().setPeerAddress(peerAddress).start().awaitUninterruptibly();
                return true;
            }
        }
        return false;
    }
    /**
     * Guarda um objecto do tipo DHTObject
     * @param object Objecto a ser guardado na dht
     * @throws IOException possivelmente se o objecto nao for serializavel
     */
    public boolean store(DHTObject object) {
        try {
            peer.put(object.getKey()).setKeyObject(object.getContentKey(), object).start().awaitUninterruptibly();
            return true;
        } catch (IOException e) {
            System.err.println("Nao foi possivel guardar o objecto:");
            System.err.println(object);
            System.err.println("Expecao " + e);
            return false;
        }
    }

    /**
     * Gets an object from the DHT
     * @param key Object Key
     * @return The Object or null if not found
     */
    public Object get(String key, DHTObjectType type) {
        Number160 hKey = Number160.createHash(key);
        FutureDHT futureDHT = peer.get(hKey).setContentKey(type.getContentKey()).start().awaitUninterruptibly();
        if (futureDHT.isSuccess()) {
            try {
                return futureDHT.getData().getObject();
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Gets an object from the DHT
     * @param key Object Key
     * @return The Object or null if not found
     */
    public Object get(Number160 key) {
        FutureDHT futureDHT = peer.get(key).start().awaitUninterruptibly();
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

    public void iterDHT() {
        for (Data data : storage.map().values()) {
            try {
                System.out.println("data = " + data.getObject());
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List<PeerAddress> getNeighbors() {
        return peer.getPeerBean().getPeerMap().getAll();
    }

    public boolean isRunning() {
        return peer.isRunning();
    }

    public void sendInfo(PeerAddress address) {

    }

    public int getPort() {
        return port;
    }
}

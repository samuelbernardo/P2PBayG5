package p2pbay.server;

import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.futures.FutureDHT;
import net.tomp2p.futures.FutureDiscover;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerMaker;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.rpc.ObjectDataReply;
import net.tomp2p.storage.Data;
import net.tomp2p.storage.StorageMemory;
import p2pbay.core.listeners.BidsListener;
import p2pbay.core.Bid;
import p2pbay.core.DHTObject;
import p2pbay.core.DHTObjectType;
import p2pbay.core.listeners.GetListener;

import java.io.IOException;
import java.net.Inet4Address;
import java.util.*;

public class TomP2PHandler {
    private Peer peer;
    private StorageMemory storage;

    public TomP2PHandler() {
        storage = new BayStorage();
    }

    /**
     * Guarda um objecto do tipo DHTObject
     * @param object Objecto a ser guardado na dht
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
     * Guarda um objecto do tipo Bid
     * @param bid Bid a ser guardado na dht
     */
    public boolean store(Bid bid) {
        // Find the most recent and the Highest Bid
        // Which is the bid with the highest position
        //TODO tirar position
        List<Bid> bids = get(bid.getTitle());
        Bid mostRecent = null;
        Bid highest = null;
        for (Bid aBid : bids) {
            if (mostRecent == null) {
                mostRecent = aBid;
                highest = aBid;
                continue;
            }
            if (mostRecent.getPosition() < aBid.getPosition())
                mostRecent = aBid;
            if (highest.getValue() < aBid.getValue())
                highest = aBid;
        }

        if (highest != null && highest.getValue() >= bid.getValue())
            return false;

        // Position generator
        // A random number that can go up 20 positions
        //from the previous highest position
        int position = new Random().nextInt(20);
        if (mostRecent != null)
            position += 1 + mostRecent.getPosition();
        bid.setPosition(position);

        // Store the bid in the DHT
        try {
            peer.add(bid.getKey()).setData(new Data(bid)).start().awaitUninterruptibly();
            return true;
        } catch (IOException e) {
            System.err.println("Nao foi possivel guardar o objecto:");
            System.err.println(bid);
            System.err.println("Excepcao " + e);
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
     * @param listener Object Key
     */
    public void get(GetListener listener, DHTObjectType type) {
        Number160 hKey = Number160.createHash(listener.getKey());
        FutureDHT futureDHT = peer.get(hKey).setContentKey(type.getContentKey()).start();
        listener.setFutureDHT(futureDHT);
        futureDHT.addListener(listener);
    }

    public int connect() throws IOException{
        int port = new Random().nextInt(5000) + 1000;
        //** CREATE A NEW PEER **//
        PeerMaker peerMaker = new PeerMaker(Number160.createHash(Inet4Address.getLocalHost().getHostAddress() + port));
        peerMaker.setStorage(storage);
        peerMaker.setPorts(port);
        peerMaker.setEnableIndirectReplication(true);
        System.out.println(peerMaker.isEnableIndirectReplication());
        peer = peerMaker.makeAndListen();

        /* Creation of a peer. */
//        peer = new PeerMaker(Number160.createHash(Inet4Address.getLocalHost().getHostAddress())).setPorts(port).makeAndListen();
        System.out.println("peer = " + peer.getPeerAddress());

        // ** Testing lambdas ** //
        // Executed when receiving a direct message.
        peer.setObjectDataReply(new ObjectDataReply() {
            @Override
            public Object reply(PeerAddress sender, Object request) throws Exception {
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
            }
        });
        return port;
    }

    public void connect(P2PBayBootstrap bootstrap) throws IOException {
        connect();

        /* Connects THIS to an existing peer. */
        System.out.println("Connecting...");

        // Procura por todos os nos dados pelo objecto P2PBayBoostrap
        for(Node node:bootstrap.getNodes()) {
            System.out.println("Trying " + node.getHostName());
            FutureDiscover futureDiscover = peer.discover().setInetAddress(node.getAddress()).setPorts(node.getPort()).start();
            futureDiscover.awaitUninterruptibly();
            FutureBootstrap fb = peer.bootstrap().setInetAddress(node.getAddress()).setPorts(node.getPort()).start();
            fb.awaitUninterruptibly();
            if (fb.getBootstrapTo() != null) {
                System.out.println("Connected to " + fb.getBootstrapTo());
                PeerAddress peerAddress = fb.getBootstrapTo().iterator().next();
                peer.discover().setPeerAddress(peerAddress).start().awaitUninterruptibly();
                break;
            }
        }
    }


    /**
     * Gets a Bid from the DHT
     * @param key Bid Key
     * @return The Bids or empty List if not found
     */
    public List<Bid> get(String key) {
        List<Bid> bidList = new ArrayList<>();
        Number160 hKey = Number160.createHash(key);
        FutureDHT futureDHT = peer.get(hKey).setAll().start().awaitUninterruptibly();
        if (futureDHT.isSuccess()) {
            try {
                for (Data map : futureDHT.getDataMap().values()) {
                    if (map.getObject() instanceof  Bid)
                        bidList.add((Bid) map.getObject());
                }
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
        }
        return bidList;
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
}

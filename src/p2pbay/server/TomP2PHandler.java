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
import p2pbay.core.Bid;
import p2pbay.core.DHTObject;
import p2pbay.core.DHTObjectType;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class TomP2PHandler {
    final private Peer peer;
    final private int port = 4001;
    private StorageMemory storage;

    final static private String test = "test";

    public TomP2PHandler(P2PBayBootstrap bootstrap) throws Exception {
        //** CREATE A NEW PEER **//
        PeerMaker peerMaker = new PeerMaker(Number160.createHash(Inet4Address.getLocalHost().getHostAddress()));
        StorageMemory storageMemory = new BayStorage();
        peerMaker.setStorage(storageMemory);
        peerMaker.setPorts(port);
        peerMaker.setEnableIndirectReplication(true);
        System.out.println(peerMaker.isEnableIndirectReplication());
        storage = storageMemory;
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



        /* Connects THIS to an existing peer. */
        System.out.println("Connecting...");


        // Procura por todos os nos dados pelo objecto P2PBayBoostrap
        for(InetAddress address:bootstrap.getNodes()) {
            System.out.println("Trying " + address.getHostName());
            FutureDiscover futureDiscover = peer.discover().setInetAddress(address).setPorts(port).start();
            futureDiscover.awaitUninterruptibly();
            FutureBootstrap fb = peer.bootstrap().setInetAddress(address).setPorts(port).start();
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
        try {
            System.out.println("bid = " + bid.getKey());
            peer.add(bid.getKey()).setData(new Data(bid)).start().awaitUninterruptibly();
            return true;
        } catch (IOException e) {
            System.err.println("Nao foi possivel guardar o objecto:");
            System.err.println(bid);
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
     * Gets a Bid from the DHT
     * @param key Bid Key
     * @return The Bids or null if not found
     */
    public List<Bid> get(String key) {
        Number160 hKey = Number160.createHash(key);
        FutureDHT futureDHT = peer.get(hKey).setAll().start().awaitUninterruptibly();
        ArrayList<Bid> bids = new ArrayList<>();
        if (futureDHT.isSuccess()) {
            try {
                for (Data map : futureDHT.getDataMap().values()) {
                    if (map.getObject() instanceof  Bid)
                        bids.add((Bid) map.getObject());
                }
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
        }
        return bids;
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

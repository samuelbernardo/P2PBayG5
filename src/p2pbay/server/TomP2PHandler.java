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

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;

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
        storage = storageMemory;
        peer = peerMaker.makeAndListen();

        /* Creation of a peer. */
//        peer = new PeerMaker(Number160.createHash(Inet4Address.getLocalHost().getHostAddress())).setPorts(port).makeAndListen();
        System.out.println("peer = " + peer.getPeerAddress());


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
     * Guarda qualquer objecto na dht
     * @param key String que deve ser usada como chave
     * @param object Objecto a ser guardado na dht
     * @throws IOException possivelmente se o objecto nao for serializavel
     */
    public boolean store(String key, Object object) {
        try {
            Number160 hKey = Number160.createHash(key);
            peer.put(hKey).setData(new Data(object)).start().awaitUninterruptibly();
            return true;
        } catch (IOException e) {
//            e.printStackTrace();
            System.err.println("Nao foi possivel guardar o objecto:");
            System.err.println(object);
            System.err.println("Expecao " + e);
            return false;
        }
    }

    public boolean store(DHTObject object) {
        try {
            peer.put(object.getKey()).setData(new Data(object)).start().awaitUninterruptibly();
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
}

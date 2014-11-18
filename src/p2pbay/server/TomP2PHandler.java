package p2pbay.server;

import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.futures.FutureDHT;
import net.tomp2p.futures.FutureDiscover;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerMaker;
import net.tomp2p.peers.Number160;


import p2pbay.core.Bid;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.List;

public class TomP2PHandler {
    final private Peer peer;
    final private int port = 4001;

    public TomP2PHandler(P2PBayBootstrap bootstrap) throws Exception {
        /* Creation of a peer. */
        peer = new PeerMaker(Number160.createHash(Inet4Address.getLocalHost().getHostAddress())).setPorts(port).makeAndListen();
        System.out.println("peer = " + peer.getPeerAddress());

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
                peer.discover().setPeerAddress(fb.getBootstrapTo().iterator().next()).start().awaitUninterruptibly();
                break;
            }
        }
    }

    /*public Object get(String locationKey, String contentKey) throws ClassNotFoundException, IOException {
        Number160 hKey = Number160.createHash(locationKey);
        FutureDHT futureDHT = peer.get(hKey).setContentKey(Number160.createHash(contentKey)).start();
        futureDHT.awaitUninterruptibly();
        if (futureDHT.isSuccess()) {
            return futureDHT.getData().getObject();
        }
        return "Not found!";
    }*/
    
    public Object get(String key) throws ClassNotFoundException, IOException {
        Number160 hKey = Number160.createHash(key);
        FutureDHT futureDHT = peer.get(hKey).start().awaitUninterruptibly();
        if (futureDHT.isSuccess()) {
            return futureDHT.getData().getObject();
        }
        return "Not found!";
    }
    
    
    /*public void storeUser(User user) throws IOException {
        Number160 key = Number160.createHash(user.getUsername());
        peer.put(key).setData(new Data(user)).start().awaitUninterruptibly();
    }*/
    
    /*public void storeUser(User user) throws IOException {
        Number160 key = Number160.createHash(user.getUsername());
        peer.put(key).setKeyObject(Number160.createHash("pass"), user.getPassword()).start();
        peer.put(key).setKeyObject(Number160.createHash("bids"), user.getBids()).start();
    }*/
    
    /*public void storeItem(Item item) throws IOException {
        Number160 key = Number160.createHash(item.getTitle());
        peer.put(key).setKeyObject(Number160.createHash("title"), item.getTitle()).start();
        peer.put(key).setKeyObject(Number160.createHash("description"), item.getDescription()).start();
        peer.put(key).setKeyObject(Number160.createHash("owner"), item.getOwner()).start();
        peer.put(key).setKeyObject(Number160.createHash("auctionStatus"), item.isAuctionClosed()).start();
        peer.put(key).setKeyObject(Number160.createHash("bids"), item.getBids()).start();
    }*/

    public void storeNewBid(String locationKey, String contentKey, List<Bid> bids) throws IOException {
        Number160 key = Number160.createHash(locationKey);
        peer.put(key).setKeyObject(Number160.createHash(contentKey), bids).start();
    }

    public void closeAuction(String locationKey) throws IOException {
        Number160 key = Number160.createHash(locationKey);
        peer.put(key).setKeyObject(Number160.createHash("auctionStatus"), true).start();
    }

    /**
     * Guarda qualquer objecto na dht
     * @param key String que deve ser usada como chave
     * @param object Objecto a ser guardado na dht
     * @throws IOException possivelmente se o objecto nao for serializavel
     */
    public void store(String key, Object object) throws IOException {
        Number160 keyHash = Number160.createHash(key);
        peer.put(keyHash).setObject(object);
    }
}

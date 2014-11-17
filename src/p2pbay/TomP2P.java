package p2pbay;

import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.futures.FutureDHT;
import net.tomp2p.futures.FutureDiscover;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerMaker;
import net.tomp2p.peers.Number160;
import p2pbay.core.Bid;
import p2pbay.core.Item;
import p2pbay.core.User;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.List;

public class TomP2P {
    final private Peer peer;
    final private int port = 4001;

    public TomP2P() throws Exception {
        peer = new PeerMaker(Number160.createHash(Inet4Address.getLocalHost().getHostAddress())).setPorts(port).makeAndListen();

        InetAddress address = Inet4Address.getLocalHost();
        FutureDiscover futureDiscover = peer.discover().setInetAddress(address).setPorts(port).start();
        futureDiscover.awaitUninterruptibly();

        FutureBootstrap fb = peer.bootstrap().setInetAddress(address).setPorts(port).start();

        fb.awaitUninterruptibly();
        if (fb.getBootstrapTo() != null) {
            peer.discover().setPeerAddress(fb.getBootstrapTo().iterator().next()).start().awaitUninterruptibly();
        }
    }

    Object get(String locationKey, String contentKey) throws ClassNotFoundException, IOException {
        Number160 hKey = Number160.createHash(locationKey);
        FutureDHT futureDHT = peer.get(hKey).setContentKey(Number160.createHash(contentKey)).start();
        futureDHT.awaitUninterruptibly();
        if (futureDHT.isSuccess()) {
            return futureDHT.getData().getObject();
        }
        return "Not found!";
    }

    void storeUser(User user) throws IOException {
        Number160 key = Number160.createHash(user.getUsername());
        peer.put(key).setKeyObject(Number160.createHash("pass"), user.getPassword()).start();
        peer.put(key).setKeyObject(Number160.createHash("bids"), user.getBids()).start();
    }

    void storeItem(Item item) throws IOException {
        Number160 key = Number160.createHash(item.getTitle());
        peer.put(key).setKeyObject(Number160.createHash("title"), item.getTitle()).start();
        peer.put(key).setKeyObject(Number160.createHash("description"), item.getDescription()).start();
        peer.put(key).setKeyObject(Number160.createHash("owner"), item.getOwner()).start();
        peer.put(key).setKeyObject(Number160.createHash("auctionStatus"), item.isAuctionClosed()).start();
        peer.put(key).setKeyObject(Number160.createHash("bids"), item.getBids()).start();
    }

    void storeNewBid(String locationKey, String contentKey, List<Bid> bids) throws IOException {
        Number160 key = Number160.createHash(locationKey);
        peer.put(key).setKeyObject(Number160.createHash(contentKey), bids).start();
    }

    void closeAuction(String locationKey) throws IOException {
        Number160 key = Number160.createHash(locationKey);
        peer.put(key).setKeyObject(Number160.createHash("auctionStatus"), true).start();
    }
}

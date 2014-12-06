package p2pbay.server;

import gossipico.*;
import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.futures.FutureDHT;
import net.tomp2p.futures.FutureDiscover;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerMaker;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.storage.Data;
import net.tomp2p.storage.StorageMemory;
import p2pbay.core.Bid;
import p2pbay.core.DHTObject;
import p2pbay.core.DHTObjectType;
import p2pbay.core.listener.GetListener;
import p2pbay.server.messages.Message;
import p2pbay.server.messages.Receiver;
import p2pbay.server.messages.Sender;

import java.io.IOException;
import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TomP2PHandler {
    private Peer peer;
    private StorageMemory storage;
    private int port = 1234;

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

    public boolean nonBlockstore(DHTObject object) {
        try {
            peer.put(object.getKey()).setKeyObject(object.getContentKey(), object).start();
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
        List<Bid> bids = get(bid.getTitle());
        Bid highest = null;
        for (Bid aBid : bids) {
            if (highest == null) {
                highest = aBid;
                continue;
            }
            if (highest.getValue() < aBid.getValue())
                highest = aBid;
        }

        if (highest != null && highest.getValue() >= bid.getValue())
            return false;

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

    public Number160 getPeerId(String key, DHTObjectType type) {
        Number160 hKey = Number160.createHash(key);
        FutureDHT futureDHT = peer.get(hKey).setContentKey(type.getContentKey()).start().awaitUninterruptibly();
        if (futureDHT.isSuccess()) {
            return futureDHT.getData().getPeerId();
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

    public int connect(boolean randomPort) throws IOException{
        if (randomPort)
            port = new Random().nextInt(20000) + 1234;
        //** CREATE A NEW PEER **//
        PeerMaker peerMaker = new PeerMaker(Number160.createHash(Inet4Address.getLocalHost().getHostAddress() + port));
        peerMaker.setStorage(storage);
        peerMaker.setPorts(port);
        peerMaker.setEnableIndirectReplication(true);
        peer = peerMaker.makeAndListen();

        /* Creation of a peer. */
//        peer = new PeerMaker(Number160.createHash(Inet4Address.getLocalHost().getHostAddress())).setPorts(port).makeAndListen();
        System.out.println("peer = " + peer.getPeerAddress());

        // Executed when receiving a direct message.
        peer.setObjectDataReply(new Receiver());
        return peerMaker.getTcpPort();
    }

    public int connect(P2PBayBootstrap bootstrap, boolean randomPort) throws IOException {
        int port = connect(randomPort);

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
        return port;
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
        System.out.println("Cya");
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

    public void shutdowmNetwork() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                new Sender(peer, Message.SHUTDOWM).broadcast();
                close();
            }
        }).start();
    }

    public List<PeerAddress> getNeighbors() {
        return peer.getPeerBean().getPeerMap().getAll();
    }

    public boolean isRunning() {
        return peer.isRunning();
    }

    public void sendInfo(PeerAddress address) {
        peer.sendDirect(address).setObject(infoMessage).start();
    }

    public void send(Message message, PeerAddress peer) {
        this.peer.sendDirect(peer).setObject(message).start();
    }

    public void addInfo(DHTObject object) {
        switch (object.getType()) {
            case ITEM:
                infoMessage.addItem(object.getKey());
                break;
            case USER:
                infoMessage.addUser(object.getKey());
                break;
        }
    }

    public int getPort() {
        return peer.getPeerAddress().portTCP();
    }

    /**
     *
     * @return
     */
    public int getNetworkSize() {
        return  bootstrap.getNodes().size();
    }

    /**
     *
     * @return
     */
    public ArrayList<Node> getNodes() {
        return bootstrap.getNodes();
    }


    public SystemInfoMessage getSystemInfo() {
        return infoMessage;
    }

    /**
     *
     * @param countModule
     */
    public void setCountModule(CountModule countModule) {
        this.countModule = countModule;
    }

    /**
     *
     * @return
     */
    public CountBeaconModule getCountBeaconModule() {
        return (CountBeaconModule)this.countModule;
    }

    /**
     *
     * @param countBeaconModule
     */
    public void setCountBeaconModule(CountBeaconModule countBeaconModule) {
        this.countModule = countBeaconModule;
    }

    /**
     *
     * @return
     */
    public CountModule getCountModule() {
        return this.countModule;
    }

    /**
     *
     * @param message
     * @param peer
     */
    public void sendCountModule(gossipico.CountModule message, PeerAddress peer) {
        this.peer.sendDirect(peer).setObject(message).start();
    }

    /**
     *
     * @param message
     * @param peer
     */
    public void sendStateCount(StateMessage message, PeerAddress peer) {
        this.peer.sendDirect(peer).setObject(message).start();
    }

    public Peer getPeer() {
        return peer;
    }

    public void setPeer(Peer peer) {
        this.peer = peer;
    }

    public void sendArmyStrength(ArmyStrengthMessage message, PeerAddress peer) {
        this.peer.sendDirect(peer).setObject(message).start();
    }

    public void sendCountBeaconModuleMessage(CountBeaconModule message, PeerAddress peer) {
        this.peer.sendDirect(peer).setObject(message).start();
    }

    public void sendCountBeaconModuleAnswer(CountBeaconAnswer message, PeerAddress peer) {
        this.peer.sendDirect(peer).setObject(message).start();
    }
}

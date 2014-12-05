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
import org.jboss.netty.channel.ChannelException;
import p2pbay.core.DHTObject;
import p2pbay.core.DHTObjectType;
import p2pbay.server.messages.Message;
import p2pbay.server.messages.MessageReceiver;
import p2pbay.server.messages.SystemInfoMessage;
import p2pbay.server.monitor.ActiveMonitor;
import p2pbay.server.monitor.ServerMonitor;
import p2pbay.server.peer.Node;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TomP2PHandler {
    private Peer peer;
    private int port = 4001;
    private StorageMemory storage;
    private P2PBayBootstrap bootstrap;
    private SystemInfoMessage infoMessage;
    private CountModule countMessage;

    private ServerMonitor monitor;
    private CountModule countModule;
    //private CountBeaconModule countBeaconModule;

    public TomP2PHandler(P2PBayBootstrap bootstrap) {
        this.bootstrap = bootstrap;
        this.monitor = new ActiveMonitor();
    }

    public boolean connect() throws UnknownHostException {
        Random r = new Random();
        Number160 id = null;
        try {
            id = Number160.createHash(Inet4Address.getLocalHost().getHostAddress() + r.nextInt());
        } catch (UnknownHostException e) {
            return false;
        }
        PeerAddress localPeerAddress = new PeerAddress(id);

        //** CREATE A NEW PEER **//
        PeerMaker peerMaker = new PeerMaker(localPeerAddress.getID());
        StorageMemory storageMemory = new BayStorage(id.shortValue(), this);
        peerMaker.setStorage(storageMemory);

        peerMaker.setPorts(1000 + r.nextInt(10000));
        peerMaker.setEnableIndirectReplication(true);
        System.out.println(peerMaker.isEnableIndirectReplication());
        storage = storageMemory;
        try {
            peer = peerMaker.makeAndListen();
        } catch (IOException | ChannelException e) {
            return false;
        }
        monitor.printPeer(peer);

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

                // Troca de mensagens no TomP2P
                this.infoMessage = new SystemInfoMessage(peer.getPeerID());
                peer.setObjectDataReply(new MessageReceiver(this.infoMessage, this));

                // Criar Thread para o peer
                new InfoThread(this).start();

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

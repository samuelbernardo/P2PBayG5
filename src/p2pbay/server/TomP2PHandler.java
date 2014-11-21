package p2pbay.server;

import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.futures.FutureDHT;
import net.tomp2p.futures.FutureDiscover;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerMaker;
import net.tomp2p.peers.Number160;
import net.tomp2p.storage.Data;
import p2pbay.core.DHTObject;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;

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
            peer.put(object.getHash()).setData(new Data(object)).start().awaitUninterruptibly();
            return true;
        } catch (IOException e) {
            System.err.println("Nao foi possivel guardar o objecto:");
            System.err.println(object);
            System.err.println("Expecao " + e);
            return false;
        }
    }
    
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

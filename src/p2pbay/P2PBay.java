package p2pbay;

import gossipico.CountBeaconInitializer;
import p2pbay.client.Client;
import p2pbay.server.P2PBayBootstrap;
import p2pbay.server.TomP2PHandler;

import java.util.ArrayList;

public class P2PBay {
    public static TomP2PHandler P2PBAY;
    public static ArrayList<TomP2PHandler> nodes =  new ArrayList<>();
    public static Client client;

    public static void close() {
        P2PBAY.close();
        for (TomP2PHandler node : nodes) {
            node.close();
        }
    }

    public static void main(String[] args) throws Exception {
        //Connect to the P2P network
        P2PBayBootstrap bootstrap = new P2PBayBootstrap();
        bootstrap.loadConfig();
        P2PBAY = new TomP2PHandler();
        int port = P2PBAY.connect(bootstrap, false);
//        int port = P2PBAY.connect(false);

        client = null;
        if(args.length > 0) {
            for (String arg : args) {
                switch (arg) {
                    case "server":
                        //If the program is running as a server, there is no menu
                        return;
                    case "dev":
                        client = new Client(P2PBAY, true);
                }
            }
        }
        //Running in client mode
        if (client == null)
            client = new Client(P2PBAY);


        /**
         * The commented code that
         * follows is used to run test
         */
//        bootstrap.addLocal(port);
//
//        for (int i = 0; i < 100; i++) {
//            TomP2PHandler handler = new TomP2PHandler();
//            handler.connect(bootstrap, true);
//            nodes.add(handler);
//        }
//
//        CountThread count = new CountThread();
//        IDCount idCount = new IDCount();
//        for (int i = 0; i < 10; i++) {
//            new UserCreation(i, 20, count, idCount).start();
//        }
//        Thread.sleep(50000);
//        Random r = new Random();
//        CountThread countThread = new CountThread();
//        for (int i = 0; i < 20; i++) {
//            new UserAccess(100, 10*20, countThread, nodes.get(r.nextInt(nodes.size()))).start();
//        }
        
        // Gossipico start
        new CountBeaconInitializer(P2PBAY).start();

        client.start();
    }
}
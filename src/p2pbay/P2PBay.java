package p2pbay;

import p2pbay.client.Client;
import p2pbay.server.P2PBayBootstrap;
import p2pbay.server.TomP2PHandler;
import p2pbay.testing.AutoTest;

import java.util.ArrayList;

public class P2PBay {
    public static TomP2PHandler P2PBAY;
    public static ArrayList<TomP2PHandler> nodes =  new ArrayList<>();

    public static void close() {
        P2PBAY.close();
        for (TomP2PHandler node : nodes) {
            node.close();
        }
    }

    public static void main(String[] args) throws Exception {
        //Connect to the P2P network
        P2PBayBootstrap bootstrap = new P2PBayBootstrap();
//        bootstrap.loadConfig();
        P2PBAY = new TomP2PHandler();
        int port = P2PBAY.connect();

        Client client = null;
        //If the program is running as a server, there is no menu
        if(args.length > 0) {
            for (String arg : args) {
                switch (arg) {
                    case "server":
                        return;
                    case "dev":
                        client = new Client(P2PBAY, true);
                }
            }
        }
        //Running in client mode
        if (client == null)
            client = new Client(P2PBAY);

        bootstrap.addLocal(port);

        for (int i = 0; i < 10; i++) {
            TomP2PHandler handler = new TomP2PHandler();
            handler.connect(bootstrap);
            nodes.add(handler);
        }

        AutoTest.storeItems();

        client.start();
    }
}
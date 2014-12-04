package p2pbay;

import p2pbay.client.Client;
import p2pbay.server.P2PBayBootstrap;
import p2pbay.server.TomP2PHandler;

public class P2PBay {
    public static TomP2PHandler P2PBAY;

    public static void main(String[] args) throws Exception {
        //Connect to the P2P network
        P2PBayBootstrap bootstrap = new P2PBayBootstrap();
        //bootstrap.loadConfig();
        P2PBAY = new TomP2PHandler(bootstrap);

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
        int nodes = 10;

        for (int i = 0; i < nodes; i++) {
//            new TomP2PHandler(bootstrap);
        }

        //Running in client mode
        if (client == null)
            client = new Client(P2PBAY);

        client.start();
    }
}
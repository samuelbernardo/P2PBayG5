package p2pbay;

import p2pbay.client.Client;
import p2pbay.server.P2PBayBootstrap;
import p2pbay.server.TomP2PHandler;

public class P2PBay {
    public static TomP2PHandler P2PBAY;

    public static void main(String[] args) throws Exception {
        boolean serverMode = false;
        boolean verbose = true;
        int port = 4001;

        for (String arg : args) {
            switch (arg) {
                case "server":
                    serverMode = true;
                    break;
                case "-s":
                    verbose = false;
                    break;
                default:
                    try {
                        port = Integer.parseInt(arg);
                    } catch (NumberFormatException ignore) {}
            }
        }

        //Connect to the P2P network
        P2PBayBootstrap bootstrap = new P2PBayBootstrap();
//        bootstrap.loadConfig();
        while(bootstrap.loadLocalPort());

        P2PBAY = new TomP2PHandler(bootstrap);
//        P2PBAY = new TomP2PHandler(bootstrap, port, verbose);
        System.out.println("Connecting...");
        P2PBAY.connect();
        System.out.println("Listening on port " + P2PBAY.getPort());
        bootstrap.addLocalPort(P2PBAY.getPort());
        for(int i = 0; i < 100; i++) {
            TomP2PHandler handler = new TomP2PHandler(bootstrap);
            while(!handler.connect());
            System.out.println("i = " + i);
        }
        if (serverMode)
            return;

        //Running in client mode
        Client client = new Client(P2PBAY);
        client.start();
    }
}
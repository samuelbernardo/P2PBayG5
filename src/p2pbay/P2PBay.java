package p2pbay;

import p2pbay.client.Client;
import p2pbay.server.P2PBayBootstrap;
import p2pbay.server.TomP2PHandler;

public class P2PBay {
    public static TomP2PHandler P2PBAY;

    public static void main(String[] args) throws Exception {
        //Connect to the P2P network
        P2PBayBootstrap bootstrap = new P2PBayBootstrap();
        bootstrap.loadConfig();
        P2PBAY = new TomP2PHandler(bootstrap);

        //If the program is running as a server, there is no menu
        if(args.length > 0) {
            if(args[0].equals("server"))
                return;
        }

        //Running in client mode
        Client client = new Client(P2PBAY);
        client.start();
    }
}
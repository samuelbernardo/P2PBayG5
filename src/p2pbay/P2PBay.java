package p2pbay;

import java.util.Scanner;

import p2pbay.client.Client;
import p2pbay.server.P2PBayBootstrap;
import p2pbay.server.TomP2PHandler;

public class P2PBay {

    public static void main(String[] args) throws Exception {
        //Connect to the P2P network
        P2PBayBootstrap bootstrap = new P2PBayBootstrap();
        bootstrap.loadConfig();
        
        TomP2PHandler CONNECTIONHANDLER = new TomP2PHandler(bootstrap);

        //If the program is running as a server, there is no menu
        if(args.length > 0) {
            if(args[0].equals("server"))
                return;
        }
        else {
            //Running in client mode
            Scanner inputReader = new Scanner(System.in);
            Client client = new Client(CONNECTIONHANDLER, inputReader);
            client.start();
        }
    }
}
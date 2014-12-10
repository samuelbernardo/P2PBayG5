package p2pbay.client.user.commands;

import gossipico.CountBeaconModule;
import p2pbay.client.Client;
import p2pbay.client.user.UserInteraction;
import p2pbay.core.Bid;
import p2pbay.core.User;

public class StatisticsCheck extends UserInteraction {
    private CountBeaconModule beaconModule;

    public StatisticsCheck(Client client) {
        super(client);
    }

    @Override
    public void doOperation() {
        if(this.beaconModule != null) {
            System.out.println("Number of nodes: " + this.beaconModule.getWaiting().getValue());
            System.out.println("Number of users: " + this.beaconModule.getWaiting().getUser_value());
            System.out.println("Number of items on sale: " + this.beaconModule.getWaiting().getItem_value());
        }
        else {
            System.out.println("Number of nodes: null");
            System.out.println("Number of users: null");
            System.out.println("Number of items on sale: null");
        }
    }

    @Override
    public void getInfo() {
        this.beaconModule = getClient().getCountBeacon();
    }
}
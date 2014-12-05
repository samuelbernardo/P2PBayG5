package p2pbay.client.commands;

import gossipico.CountBeaconModule;
import p2pbay.client.Client;
import p2pbay.client.user.UserInteraction;
import p2pbay.core.Bid;
import p2pbay.core.User;

public class StatisticsCheck extends UserInteraction{

    public StatisticsCheck(Client client) {
        super(client);
    }

    @Override
    public void getInfo() {
        CountBeaconModule beaconModule = getClient().getCountBeacon();
        System.out.println("Number of nodes: " + beaconModule.getWaiting().getValue());
        System.out.println("Number of users: ");
        System.out.println("Number of items on sale: ");
    }

    @Override
    public void storeObjects() {}
}
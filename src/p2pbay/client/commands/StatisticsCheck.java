package p2pbay.client.commands;

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
        User user = getClient().getUser(getClient().getUser().getUsername());
        System.out.println("Titulo:\t\tValor:");
        for(Bid b : user.getBids()) {
            System.out.println(b.getTitle() + "\t\t" + b.getValue());
        }
    }

    @Override
    public void storeObjects() {}
}
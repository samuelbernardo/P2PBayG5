package p2pbay.client.user.commands;

import p2pbay.client.Client;
import p2pbay.client.user.UserInteraction;
import p2pbay.core.Bid;
import p2pbay.core.User;

public class HistoryCheck extends UserInteraction{

    public HistoryCheck(Client client) {
        super(client);
    }

    @Override
    public void getInfo() {
        User user = getClient().getUser();
        System.out.println("Titulo:\t\tValor:");
        for(Bid b : user.getBids()) {
            System.out.println(b.getTitle() + "\t\t" + b.getValue());
        }
    }

    @Override
    public void storeObjects() {}
}
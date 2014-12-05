package p2pbay.client.user.commands;

import p2pbay.client.Client;
import p2pbay.client.SysStrings;
import p2pbay.client.user.UserInteraction;
import p2pbay.core.Bid;
import p2pbay.core.User;

public class HistoryCheck extends UserInteraction{
    private User user;
    
    public HistoryCheck(Client client) {
        super(client);
    }

    @Override
    public void getInfo() {
        user = getClient().getUser();
    }

    @Override
    public void doOperation() {
        System.out.println(SysStrings.HISTORY);
        for(Bid b : user.getBids()) {
            System.out.println(SysStrings.ITEMS_BIDDED);
            System.out.println(b.getTitle() + "\t\t" + b.getValue());
        }
    }
}

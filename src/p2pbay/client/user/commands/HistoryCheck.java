package p2pbay.client.user.commands;

import p2pbay.client.Client;
import p2pbay.client.SysStrings;
import p2pbay.client.user.UserInteraction;
import p2pbay.core.Bid;
import p2pbay.core.Item;
import p2pbay.core.User;

/**
 * Comando para ver o historico do utilizador logado no sistema
 */
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
        System.out.println(SysStrings.ITEMS_BIDDED);
        System.out.println(SysStrings.HISTORY);
        for(Bid b : user.getBids()) {
            System.out.println(b.getTitle() + "\t\t" + b.getValueToString());
        }
        System.out.println(SysStrings.ITEMS_BOUGHT);
        System.out.println(SysStrings.HISTORY);
        for(Item i : user.getBoughtItems())
            System.out.println(i.getTitle() + "\t\t" + i.getValueToString());
    }
}

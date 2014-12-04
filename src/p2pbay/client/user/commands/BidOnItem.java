package p2pbay.client.user.commands;

import p2pbay.client.Client;
import p2pbay.client.SysStrings;
import p2pbay.client.user.UserInteraction;
import p2pbay.core.Bid;
import p2pbay.core.Item;

public class BidOnItem  extends UserInteraction{
    private String title;
    private Float proposedValue;

    public BidOnItem(Client client) {
        super(client);
    }

    @Override
    public void getInfo() {
        System.out.print("Titulo do item:");
        this.title = getInput();
        this.proposedValue = getPositiveNumber(SysStrings.VALUE);
    }

    @Override
    public void storeObjects() {
        Item item = getClient().getItem(title);
        if(item == null) {
            System.out.println("O item que pretende licitar nao existe...");
            return;
        }
        if (proposedValue > item.getValue()) {
            Bid newBid =  new Bid(title, getClient().getUser(), proposedValue);

            getClient().getUser().addBid(newBid);
            getClient().store(getClient().getUser());
            if (getClient().storeBid(newBid))
                System.out.println("A licitacao foi aceite!");
        }
        else
            System.out.println("A licitacao foi rejeitada pois o valor do item (" +
                                item.getValueToString() +
                                ") e igual ou superior a sua oferta!");
    }
}
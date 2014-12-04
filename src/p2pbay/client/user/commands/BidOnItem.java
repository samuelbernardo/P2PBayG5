package p2pbay.client.user.commands;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import p2pbay.client.Client;
import p2pbay.client.SysStrings;
import p2pbay.client.user.UserInteraction;
import p2pbay.core.Bid;
import p2pbay.core.Item;

import java.util.List;

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
    public void doOperation() {
        Item item = getClient().getItem(title);
        if (isValid(item)) {
            Bid newBid = new Bid(title, getClient().getUser(), proposedValue);
            getClient().store(getClient().getUser());
            if (getClient().storeBid(newBid)) {
                getClient().getUser().addBid(newBid);
                System.out.println(SysStrings.BID_ACCEPTED);
            } else {
                System.err.println(SysStrings.BID_REJECTED);
            }
        }
    }

    private boolean isValid(Item item) {
        if (item == null) {
            System.err.println(SysStrings.ITEM_NOT_EXIST);
            return false;
        }
        if (proposedValue <= item.getValue()) {
            System.err.println(SysStrings.BID_BAD_VALUE_1 + item.getValueToString() + SysStrings.BID_BAD_VALUE_2);
            return false;
        }
        if (item.auctionIsClosed()) {
            System.err.println(SysStrings.BID_AUCTION_CLOSED);
            return false;
        }
        return true;
    }


}
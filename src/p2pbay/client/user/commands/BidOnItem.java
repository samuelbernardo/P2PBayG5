package p2pbay.client.user.commands;

import p2pbay.client.Client;
import p2pbay.client.SysStrings;
import p2pbay.client.user.UserInteraction;
import p2pbay.core.Bid;
import p2pbay.core.Item;
import p2pbay.core.User;

public class BidOnItem  extends UserInteraction{
    private String title;
    private Float proposedValue;
    private Item item;

    public BidOnItem(Client client) {
        super(client);
    }

    @Override
    public void getInfo() {
        System.out.print("Titulo do item:");
        title = readInput();
        proposedValue = getPositiveNumber(SysStrings.VALUE);
        item = getClient().getItem(title);
    }

    @Override
    public void doOperation() {
        if (isValid(item)) {
            Bid newBid = new Bid(title, getClient().getLogged(), proposedValue);
            if (getClient().storeBid(newBid)) {
                User user = getClient().getUser();
                user.addBid(newBid);
                getClient().store(user);
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
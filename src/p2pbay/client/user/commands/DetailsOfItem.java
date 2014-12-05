package p2pbay.client.user.commands;

import p2pbay.client.Client;
import p2pbay.client.SysStrings;
import p2pbay.client.user.UserInteraction;
import p2pbay.core.Bid;
import p2pbay.core.Item;

import java.util.Collections;
import java.util.List;

/**
 * Comando para verificar os detalhes de um item
 */
public class DetailsOfItem extends UserInteraction {
    private Item item;
    private List<Bid> bids;

    public DetailsOfItem(Client client) {
        super(client);
    }

    @Override
    public void getInfo() {
        item = getClient().getItem(readInput(SysStrings.INPUT_TITLE));
    }

    @Override
    public void doOperation() {
        if(isValid()){
            System.out.println(SysStrings.INPUT_TITLE + item.getTitle());
            System.out.println(SysStrings.INPUT_DESCRIPTION + item.getDescription());
            bids = getClient().getBids(item.getTitle());
            Collections.sort(bids);
            System.out.print(SysStrings.VALUE);
            System.out.println(bids.get(bids.size() - 1).getValue()); // bids.get(bids.size()-1) = Latest and Highest Bid
            System.out.println(SysStrings.BIDS);
            printBids();
        }
    }

    private boolean isValid() {
        if(item == null) {
            System.out.println(SysStrings.ITEM_NOT_EXIST);
            return false;
        }
        else
            return true;
    }

    private void printHighestBid() {
        Bid highestBid = getClient().getHighestBid(bids);

        if (highestBid != null)
            System.out.println(highestBid.getValueToString());
        else
            System.out.println(item.getValueToString());
    }

    private void printBids() {
        for (Bid bid : bids) {
            System.out.println(SysStrings.INPUT_USERNAME + bid.getOwner() + " " + SysStrings.VALUE + bid.getValue());
        }
    }
}

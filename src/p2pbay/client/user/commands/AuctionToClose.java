package p2pbay.client.user.commands;

import p2pbay.client.Client;
import p2pbay.client.SysStrings;
import p2pbay.client.user.UserInteraction;
import p2pbay.core.Bid;
import p2pbay.core.Index;
import p2pbay.core.Item;
import p2pbay.core.listeners.GetListener;

import java.util.List;

/**
 * Comando para fechar um leilao
 */
public class AuctionToClose extends UserInteraction{
    private String title;
    private List<Bid> bids;
    private Item item;

    public AuctionToClose(Client client) {
        super(client);
    }

    @Override
    public void getInfo() {
        System.out.print("\n" + SysStrings.INPUT_TITLE);
        title = readInput();
    }
    
    @Override
    public void doOperation() {
        if(isValid()) {
            item.setAuctionClosed(true);
            Bid highestBid = getClient().getHighestBid(bids);
            String value = highestBid.getValueToString();
            String owner = highestBid.getOwner();
            if(getClient().store(item)) {
                System.out.print(SysStrings.AUCTION_CLOSED + value);
                System.out.println(SysStrings.AUCTION_WINNER + owner);
                item.setValue(highestBid.getValue());
                getClient().getUser(owner).addItem(item);
                removeTitle();
            }
            else {
                System.out.println(SysStrings.ITEM_ERROR);
            }
        }
    }
    
    private boolean isValid() {
        item = getClient().getItem(title);
        if (item == null) {
            System.err.println(SysStrings.ITEM_NOT_EXIST);
            return false;
        }
        if (!item.getOwner().equals(getClient().getUser().getUsername())) {
            System.err.println(SysStrings.ITEM_NOT_OWNER);
            return false;
        }
        bids = getClient().getBids(item.getTitle());
        if (bids.isEmpty()) {
            System.err.println(SysStrings.AUCTION_NO_BIDS);
            return false;
        }
        return true;
    }
    
    private void removeTitle() {
        for(String term : this.title.split(" ")) {
            removeIndex(term);
        }
    }
    
    private void removeIndex(String term) {
        GetListener getListener = new GetListener(term);
        getClient().getIndex(getListener);
        Index index = (Index) getListener.getObject();
        if (index != null) {
            index.removeTitle(this.title);
            getClient().store(index);
        }
    }

}
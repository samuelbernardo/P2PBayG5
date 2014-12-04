package p2pbay.client.user.commands;

import p2pbay.client.Client;
import p2pbay.client.SysStrings;
import p2pbay.client.user.UserInteraction;
import p2pbay.core.Index;
import p2pbay.core.Item;

public class AuctionToClose extends UserInteraction{
    private String title;

    public AuctionToClose(Client client) {
        super(client);
    }

    @Override
    public void getInfo() {
        System.out.print("\n" + SysStrings.INPUT_TITLE);
        title = getInput();
    }
    
    private boolean isValid(Item item) {
        if (item == null) {
            System.err.println(SysStrings.ITEM_NOT_EXIST);
            return false;
        }
        if (!item.getOwner().equals(getClient().getUser().getUsername())) {
            System.err.println(SysStrings.ITEM_NOT_OWNER);
            return false;
        }
        return true;
    }
    
    @Override
    public void storeObjects() {
        Item item = getClient().getItem(title);

        
        if(isValid(item)) {
            item.setAuctionClosed(true);
            float value = item.getValue();
            if(getClient().store(item))
                System.out.println(SysStrings.ACTION_CLOSED + item.getValueToString());
            else {
                System.out.println(SysStrings.ITEM_ERROR);
            }
        }

        removeTitle();
    }
    
    private void removeTitle() {
        for(String term : this.title.split(" ")) {
            removeIndex(term);
        }
    }
    
    public void removeIndex(String term) {
        Index index = getClient().getIndex(term);
        if (index != null) {
            index.removeTitle(this.title);
            getClient().store(index);//TODO not checked
        }
    }
    
    public void printIndex() {
        for(String term : this.title.split(" ")) {
            Index index = getClient().getIndex(term);
            if (index != null)
                System.out.println(SysStrings.TERM + ": " + index.getTerm() +
                                   SysStrings.TITLE + ": " + index.getTitles());
        }
    }
}
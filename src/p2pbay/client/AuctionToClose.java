package p2pbay.client;

import p2pbay.client.user.UserInteraction;
import p2pbay.core.Item;

public class AuctionToClose extends UserInteraction{
    private String title;

    public AuctionToClose(Client client) {
        super(client);
    }

    @Override
    public void getInfo() {
        System.out.println("\nTitulo:");
        title = getInput();
    }
    
    private boolean isValid(Item item) {
        item = getClient().getItem(title);
        if (item == null) {
            System.err.println("The item doesn't exist");
            return false;
        }
        if (!item.getOwner().equals(user.getUsername())) {
            System.err.println("You can't close this auction because you're not the owner of the item");
            return false;
        }
        return true;
    }
    
    @Override
    public void storeObjects() {
        Item item = getClient().getItem(title);
        
        if isValid(item) {
            item.setAuctionClosed(true);
            float value = item.getValue();
            if(getClient().store(item))
                System.out.println("O leilao foi fechado com sucesso, o valor final do item e " + value + "€.");
            else {
                System.out.println("Ocorreu um erro ao actualizar o item...");
            }
        }
    }
    
    private void removeTitle() {
        for(String term : this.title.split(" ")) {
            removeIndex(term);
        }
    }
    
    public void removeIndex(String term) {
        Index index = null;
        index = (Index) this.tomp2p.get(term);
        if (index != null) {
            index.removeTitle(this.title);
            try {
                tomp2p.store(term, index);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    
    public void printIndex() {
        for(String term : this.title.split(" ")) {
            Index index = (Index) tomp2p.get(term);
            if (index != null)
                System.out.println("Termo: " + index.getTerm() + "Titulo: " + index.getTitles());
        }
    }
}
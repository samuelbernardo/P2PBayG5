package p2pbay.client.user.commands;

import p2pbay.client.Client;
import p2pbay.client.SysStrings;
import p2pbay.client.user.UserInteraction;
import p2pbay.core.Index;
import p2pbay.core.Item;
import p2pbay.core.listener.GetListener;

/**
 * Comando para registar um item para venda
 */
public class ItemForSale extends UserInteraction implements Runnable {
    private String title;
    private String description;
    private float baseBid;

    public ItemForSale(Client client) {
        super(client);
    }

    @Override
    public void getInfo() {
        this.title = readInput(SysStrings.INPUT_TITLE);
        this.description = readInput(SysStrings.INPUT_DESCRIPTION);
        this.baseBid = getPositiveNumber(SysStrings.AUCTION_BASE);
    }

    @Override
    public void doOperation() {
        Item item = new Item(getClient().getUser().getUsername(), title, description, baseBid);
        if(getClient().store(item))
            System.out.println("O item foi publicado com sucesso!");
        else
            System.out.println("Ocorreu um erro ao publicar o item...");

        indexItems();
    }

    public void indexItems() {
        for(String term : title.split(" "))
            indexTerm(term);
    }

    public void indexTerm(String term) {
        GetListener getListener = new GetListener(term);
        getClient().getIndex(getListener);
        Index index = (Index) getListener.getObject();

        if (index == null)
            index = new Index(getListener.getKey(), title);
        else
            index.addTitle(title);

        if(getClient().store(index))
            System.out.println("Term:" + index.getTerm() + " actualizado");
        else
            System.err.println("Ocorreu um erro na insercao do indice...");
    }
}

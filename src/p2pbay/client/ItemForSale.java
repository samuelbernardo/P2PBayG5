package p2pbay.client;

import p2pbay.client.user.UserInteraction;
import p2pbay.core.Item;

public class ItemForSale extends UserInteraction implements Runnable {
    private String title;
    private String description;
    private float baseBid;

    public ItemForSale(Client client) {
        super(client);
    }

    /**
     * This method blocks waiting for user input
     */
    private void setInfo() {
        System.out.println("\nTitulo:");
        this.title = getInput();
        System.out.println("Descricao:");
        this.description = getInput();
        System.out.println("Base de licitacao:");
        this.baseBid = getFloat();
    }

    @Override
    public void run() {
        setInfo();

        Item item = new Item(getClient().getUser().getUsername(), title, description, baseBid);
        if(getClient().store(item))
            System.out.println("O item foi publicado com sucesso!");
        else
            System.out.println("Ocorreu um erro ao publicar o item...");
    }
}

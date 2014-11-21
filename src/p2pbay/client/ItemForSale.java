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

    @Override
    public void getInfo() {
        System.out.println("\nTitulo:");
        this.title = getInput();
        System.out.println("Descricao:");
        this.description = getInput();
        while (true) {
            try {
                System.out.println("Base de licitacao:");
                this.baseBid = getFloat();
                break;
            } catch (NumberFormatException e) {
                System.out.println("Numero invalido");
            }
        }
    }

    @Override
    public void storeObjects() {
        Item item = new Item(getClient().getUser().getUsername(), title, description, baseBid);
        if(getClient().store(item))
            System.out.println("O item foi publicado com sucesso!");
        else
            System.out.println("Ocorreu um erro ao publicar o item...");
    }
}

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

    public void indexItem() {
        for(String term : this.getTitle().split(" "))
            doIndex(term);
    }

    public void doIndex(String term) {
        String key = "index" + term;
        Index index = (Index) tomp2p.get(key);
        if (index == null)
            index = new Index(key, getTitle());
        else
            index.addTitle(getTitle());
        try {
            tomp2p.store(key, index);
        } catch (IOException e) {
            System.err.println("Ocorreu um erro na insercao do indice...");
        }
    }

    private float getBaseBid(Scanner input) {
        float bBid = -1;
        do{
            try {
                System.out.println("Base de licitacao:");
                bBid = Float.parseFloat(input.nextLine());
                if (bBid <= 0) {
                    System.err.println("O valor introduzido nao e valido.");
                }
            }
            catch (NumberFormatException e) {
                System.err.println("O valor introduzido nao e valido.");
            }
        } while (bBid <= 0);
        return bBid;
    }
}

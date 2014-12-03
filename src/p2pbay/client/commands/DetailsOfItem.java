package p2pbay.client.commands;

import p2pbay.client.Client;
import p2pbay.client.user.UserInteraction;
import p2pbay.core.Item;

public class DetailsOfItem extends UserInteraction {
    private Item item;

    public DetailsOfItem(Client client) {
        super(client);
    }

    @Override
    public void getInfo() {
        System.out.print("\nTitulo:");
        item = getClient().getItem(getInput());
        if(item == null) {
            System.out.println("Item nao encontrado");
        }
        System.out.println("Descricao: " + item.getDescription());
        System.out.println("Valor: " + item.getValueToString());
    }

    @Override
    public void storeObjects() {
    }
}
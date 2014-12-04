package p2pbay.client.user.commands;

import p2pbay.client.Client;
import p2pbay.client.SysStrings;
import p2pbay.client.user.UserInteraction;
import p2pbay.core.Bid;
import p2pbay.core.Item;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

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
        System.out.println("Titulo: " + item.getTitle());
        System.out.println("Descricao: " + item.getDescription());
        System.out.println("Valor: " + item.getValueToString());
        System.out.println(SysStrings.BIDS);
        printBids();
    }

    private void printBids() {
        List<Bid> bids = getClient().getBids(item.getTitle());
        Collections.sort(bids);
        for (Bid bid : bids) {
            System.out.println(SysStrings.INPUT_USERNAME + bid.getOwner() + " " + SysStrings.VALUE + bid.getValue());
        }
    }

    @Override
    public void storeObjects() {
    }
}
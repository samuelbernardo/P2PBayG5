package p2pbay.client;

import java.io.IOException;
import java.util.Scanner;
import p2pbay.core.Item;
import p2pbay.server.TomP2PHandler;

public class DetailsOfItem {
    private TomP2PHandler tomp2p;
    private String title;

    public DetailsOfItem(TomP2PHandler tomp2p, Scanner in) {
        this.tomp2p = tomp2p;
        setInfo(in);
        getDetails();
    }

    private void setInfo(Scanner in) {
        System.out.println("\nTitulo:");
        title = in.nextLine();
    }

    private void getDetails() {
        Item item = null;
        item = (Item) this.tomp2p.get(title);
        String description = item.getDescription();
        float value = item.getValue();
        System.out.println("Descricao: " + description + "\nValor: " + value + "€");
    }
}
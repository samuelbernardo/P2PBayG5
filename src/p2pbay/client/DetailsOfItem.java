package p2pbay.client;

import java.util.Scanner;
import p2pbay.core.Item;
import p2pbay.server.TomP2PHandler;

public class DetailsOfItem {
    private TomP2PHandler tomp2p;
    private String title;
    private Scanner input;

    public DetailsOfItem(TomP2PHandler tomp2p, Scanner input) {
        this.tomp2p = tomp2p;
        this.input = input;
    }
    
    public void execute() {
        setInfo();
        getDetails();
    }
    
    private void setInfo() {
        System.out.println("\nTitulo:");
        title = this.input.nextLine();
    }

    private void getDetails() {
        Item item = null;
        item = (Item) this.tomp2p.get(title);
        String description = item.getDescription();
        float value = item.getValue();
        System.out.println("Descricao: " + description + "\nValor: " + value + "€");
    }
}
package p2pbay.client;

import p2pbay.client.user.UserInteraction;
import p2pbay.core.Bid;
import p2pbay.core.Item;

public class BidOnItem  extends UserInteraction{
    private String title;
    private Float proposedValue;

    public BidOnItem(Client client) {
        super(client);
    }

    @Override
    public void getInfo() {
        System.out.print("Titulo do item:");
        this.title = getInput();
        this.proposedValue = getPositiveNumber(SysStrings.VALUE);
    }

    @Override
    public void storeObjects() {
        Item item = getClient().getItem(title);
        if(item == null) {
            System.out.println("O item que pretende licitar nao existe...");
            return;
        }
        if (proposedValue > item.getValue()) {
            Bid newBid =  new Bid(title, getClient().getUser(), proposedValue);
            item.addBid(newBid);

            //Eu sei que se esta a guardar as bids no user por causa do historico
            //Acho que e possivel modelar de maneira a so se ter de guardar um objecto
            getClient().getUser().addBid(newBid);
            if(getClient().store(item) && getClient().store(getClient().getUser()))
                //TODO e se nao for so um aceite?
                System.out.println("A licitacao foi aceite!");
            else
                System.out.println("Ocorreu um erro ao actualizar o item...");
        }
        else
            System.out.println("A licitacao foi rejeitada pois o valor do item (" +
                                item.getValueToString() +
                                ") e igual ou superior a sua oferta!");
    }
}
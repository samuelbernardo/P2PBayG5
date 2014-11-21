package p2pbay.client;

import p2pbay.core.Bid;
import p2pbay.core.User;
import p2pbay.server.TomP2PHandler;

public class HistoryCheck {

    private TomP2PHandler tomp2p;
    private User user;

    public HistoryCheck(TomP2PHandler tomp2p, User user) {
        this.tomp2p = tomp2p;
        this.user = user;
    }
    
    public void execute() {
        getHistory();
    }
    
    public void getHistory() {
        User user = null;
        user = (User) this.tomp2p.get(this.user.getUsername());
        String history = "Titulo:\t\tValor:\n";
        for(Bid b : user.getBids()) {
            history += b.getTitle() + "\t\t" + b.getValue() + "\n";
        }
        System.out.println(history);
    }
}
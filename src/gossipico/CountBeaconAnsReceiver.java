package gossipico;

import net.tomp2p.peers.PeerAddress;
import p2pbay.server.TomP2PHandler;

public class CountBeaconAnsReceiver {
    private TomP2PHandler node;
    private CountBeaconModule receivedCount;

    public CountBeaconAnsReceiver(TomP2PHandler node) {
        this.node = node;
    }

    public void process(PeerAddress sender, CountBeaconModule request) {
        this.receivedCount = request;
        CountBeaconModule countLocal = node.getCountBeaconModule();

        // Eseguo la schermaglia oppure aggiorno il percorso
        if (countLocal.army.isSameArmy(this.receivedCount.army)){
            Army.updateShortest(countLocal, this.receivedCount);
        } else {
            Army.skirmish(countLocal, this.receivedCount);
        }

    }
}

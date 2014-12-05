package gossipico;

import net.tomp2p.peers.PeerAddress;
import p2pbay.server.TomP2PHandler;

public class CountBeaconMsgReceiver {
    private TomP2PHandler node;
    private CountBeaconModule receivedCount;

    public CountBeaconMsgReceiver(TomP2PHandler node) {
        this.node = node;
    }

    public void process(PeerAddress sender, CountBeaconModule request) {
        this.receivedCount = request;
        CountBeaconModule countLocal = node.getCountBeaconModule();

        /*Army army = node.getCountBeaconModule().army;
        Message waiting = node.getCountBeaconModule().waiting;
        Message received = node.getCountBeaconModule().received;
        int state_value = node.getCountBeaconModule().state_value;
        int state_freshness = node.getCountBeaconModule().state_freshness;
        int init_value = node.getCountBeaconModule().init_value;
        List<PeerAddress> disconnected = node.getCountBeaconModule().disconnected;*/
        CountBeaconAnswer countAns = new CountBeaconAnswer(countLocal);

        // Eseguo la schermaglia oppure aggiorno il percorso
        if (countLocal.army.isSameArmy(this.receivedCount.army)){
            Army.updateShortest(countLocal, this.receivedCount);
        } else {
            Army.skirmish(countLocal, this.receivedCount);
        }

        node.sendCountBeaconModuleAnswer(countAns, sender);
    }
}

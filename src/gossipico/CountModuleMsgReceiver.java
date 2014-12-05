package gossipico;

import net.tomp2p.peers.PeerAddress;
import p2pbay.server.TomP2PHandler;

public class CountModuleMsgReceiver {
    private TomP2PHandler node;
    private CountModule receivedCount;

    public CountModuleMsgReceiver(TomP2PHandler node) {
        this.node = node;
    }

    public void process(PeerAddress sender, CountModule request) {
        this.receivedCount = request;

        node.getCountModule().received.update(this.receivedCount.waiting);

        if (node.getCountModule().updateMessage(this.receivedCount)) {
            int state_value = node.getCountModule().state_value;
            int state_freshness = node.getCountModule().state_freshness;
            StateMessage message = new StateMessage(state_value, state_freshness);

            node.sendStateCount(message, sender);
        }
    }
}

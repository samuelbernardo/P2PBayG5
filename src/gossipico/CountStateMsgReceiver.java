package gossipico;

import net.tomp2p.peers.PeerAddress;
import net.tomp2p.rpc.ObjectDataReply;
import p2pbay.server.TomP2PHandler;

public class CountStateMsgReceiver {
    private TomP2PHandler node;
    private int state_value;
    private int user_state_value;
    private int item_state_value;
    private int state_freshness;
    private StateMessage receivedState;

    public CountStateMsgReceiver(TomP2PHandler node) {
        this.node = node;
    }

    public void process(PeerAddress sender, StateMessage request) {
        this.receivedState = request;

        this.state_value = this.receivedState.getState_value();
        this.user_state_value = this.receivedState.getUser_state_value();
        this.item_state_value = this.receivedState.getItem_state_value();
        this.state_freshness = this.receivedState.getState_freshness();
        this.
        node.getCountModule().waiting.generateISMessage(this.state_value, this.state_freshness);
    }
}

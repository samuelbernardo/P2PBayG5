package gossipico;

import net.tomp2p.peers.PeerAddress;
import p2pbay.server.TomP2PHandler;

public class ArmyMsgReceiver {
    private TomP2PHandler node;
    private int strength;
    private ArmyStrengthMessage receivedState;

    public ArmyMsgReceiver(TomP2PHandler node) {
        this.node = node;
    }

    public void process(PeerAddress sender, ArmyStrengthMessage request) {
        this.receivedState = request;

        this.strength = this.receivedState.getStrength();
        node.getCountBeaconModule().army.strength = this.strength;
    }
}

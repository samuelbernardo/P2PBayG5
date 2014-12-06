package p2pbay.server.messages;

import gossipico.*;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.rpc.ObjectDataReply;
import p2pbay.server.TomP2PHandler;

public class MessageReceiver implements ObjectDataReply {
    private TomP2PHandler node;

    public MessageReceiver(TomP2PHandler node) {
        this.node = node;
    }

    @Override
    public Object reply(PeerAddress sender, Object request) throws Exception {
        if(request instanceof CountModule) {
            CountModule receivedCount = (CountModule)request;
            CountModuleMsgReceiver reply = new CountModuleMsgReceiver(node);
            reply.process(sender, receivedCount);
        }
        else  if(request instanceof StateMessage) {
            StateMessage receivedState = (StateMessage)request;
            CountStateMsgReceiver reply = new CountStateMsgReceiver(node);
            reply.process(sender, receivedState);
        }
        else  if(request instanceof ArmyStrengthMessage) {
            ArmyStrengthMessage receivedState = (ArmyStrengthMessage)request;
            ArmyStrengthMsgReceiver reply = new ArmyStrengthMsgReceiver(node);
            reply.process(sender, receivedState);
        }
        else  if(request instanceof CountBeaconModule) {
            CountBeaconModule receivedState = (CountBeaconModule)request;
            CountBeaconMsgReceiver reply = new CountBeaconMsgReceiver(node);
            reply.process(sender, receivedState);
        }
        else  if(request instanceof CountBeaconAnswer) {
            CountBeaconAnswer receivedState = (CountBeaconAnswer)request;
            CountBeaconAnsReceiver reply = new CountBeaconAnsReceiver(node);
            reply.process(sender, receivedState);
        }
//        System.out.println("sender = " + sender);
//        System.out.println("request = " + request);
        return "ok";
    }
}

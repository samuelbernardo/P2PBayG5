package p2pbay.server.messages;

import gossipico.*;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.rpc.ObjectDataReply;
import p2pbay.server.TomP2PHandler;

public class MessageReceiver implements ObjectDataReply {
    private SystemInfoMessage infoMessage;
    private TomP2PHandler node;

    public MessageReceiver(SystemInfoMessage infoMessage, TomP2PHandler node) {
        this.infoMessage = infoMessage;
        this.node = node;
    }

    private void scanInfo(SystemInfoMessage message) {
        for (Number160 id : message.getItems()) {
            infoMessage.addItem(id);
        }
        for (Number160 id : message.getUsers()) {
            infoMessage.addUser(id);
        }
        for (Number160 id : message.getNodes()) {
            infoMessage.addNode(id);
        }
        System.out.println(infoMessage);
    }

    @Override
    public Object reply(PeerAddress sender, Object request) throws Exception {
        if (request instanceof Message) {
            switch (((Message) request).getType()) {
                case TEST:
                    System.out.println("Received " + request.getClass());
                    break;
                case INFO:
//                    System.out.println("Received" + request);
                    scanInfo((SystemInfoMessage) request);
                    break;
            }
        }
        else if(request instanceof CountModule) {
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

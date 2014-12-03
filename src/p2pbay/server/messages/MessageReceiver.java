package p2pbay.server.messages;

import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.rpc.ObjectDataReply;

public class MessageReceiver implements ObjectDataReply {
    private SystemInfoMessage infoMessage;

    public MessageReceiver(SystemInfoMessage infoMessage) {
        this.infoMessage = infoMessage;
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
//        System.out.println("sender = " + sender);
//        System.out.println("request = " + request);
        return "ok";
    }
}

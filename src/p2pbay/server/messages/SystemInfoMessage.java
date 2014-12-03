package p2pbay.server.messages;

import net.tomp2p.p2p.Peer;
import net.tomp2p.peers.Number160;

import java.util.HashSet;
import java.util.Set;

public class SystemInfoMessage extends Message {
    private Number160 id;
    private boolean updated;

    private Set<Number160> nodes;
    private Set<Number160> users;
    private Set<Number160> items;

    public SystemInfoMessage(Number160 id) {
        super(MessageType.INFO);
        updated = false;
        nodes = new HashSet<>();
        users = new HashSet<>();
        items = new HashSet<>();
        this.id = id;
        addNode(id);
    }

    private boolean add(Number160 id, Set<Number160> list) {
        boolean added = list.add(id);
        updated = updated || added;
        return added;
    }

    public boolean addNode(Peer peer) {
        return add(peer.getPeerID(), nodes);
    }

    public boolean addUser(Number160 user) {
        return add(user, users);
    }

    public boolean addItem(Number160 item) {
        return add(item, items);
    }

    @Override
    public String toString() {
        return id + ":Nodes:" + nodes.size() + ":Users:" + users.size() + ":items:" + items.size();
    }

    public Set<Number160> getItems() {
        return items;
    }

    public Set<Number160> getNodes() {
        return nodes;
    }

    public Set<Number160> getUsers() {
        return users;
    }

    public boolean addNode(Number160 id) {
        return add(id, nodes);
    }

    public boolean isUpToDate() {
        return !updated;
    }

    public void setUpToDate() {
        updated = false;
    }

    public boolean isUpdated() {
        return updated;
    }
}

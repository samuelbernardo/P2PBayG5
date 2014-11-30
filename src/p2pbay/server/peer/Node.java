package p2pbay.server.peer;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Node {
    private String hostname;
    private InetAddress address = null;
    private int port;

    public Node(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    public Node(String hostname) {
        this.hostname = hostname;
        this.port = 4001;
    }

    public InetAddress getAddress() throws UnknownHostException {
        if (address == null)
            address = Inet4Address.getByName(hostname);
        return address;
    }

    public int getPort() {
        return port;
    }

    public String getHostName() {
        return hostname;
    }
}

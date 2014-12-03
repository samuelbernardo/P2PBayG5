package p2pbay.server.monitor;


public class ServerMonitors {
    public static ServerMonitor getMonitor(boolean active) {
        if (active)
            return new ActiveMonitor();
        else
            return new InactiveMonitor();
    }
}

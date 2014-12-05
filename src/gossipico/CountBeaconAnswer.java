package gossipico;

import p2pbay.server.TomP2PHandler;

import java.io.Serializable;

/**
 * For answer message identification
 */
public class CountBeaconAnswer extends CountBeaconModule implements Serializable {

    /**
     * @param myself
     */
    public CountBeaconAnswer(CountBeaconModule myself) {
        super(myself.node, myself.army, myself.waiting, myself.received, myself.state_value, myself.state_freshness, myself.init_value, myself.disconnected);
    }

}

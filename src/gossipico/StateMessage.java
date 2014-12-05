package gossipico;

import java.io.Serializable;

public class StateMessage implements Serializable {
    private int state_value;
    private int state_freshness;

    public StateMessage(int state_value, int state_freshness) {
        this.state_value = state_value;
        this.state_freshness = state_freshness;
    }

    public int getState_value() {
        return state_value;
    }

    public void setState_value(int state_value) {
        this.state_value = state_value;
    }

    public int getState_freshness() {
        return state_freshness;
    }

    public void setState_freshness(int state_freshness) {
        this.state_freshness = state_freshness;
    }
}

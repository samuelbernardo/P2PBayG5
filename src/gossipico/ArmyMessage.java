package gossipico;

import java.io.Serializable;

public class ArmyMessage implements Serializable {
    private Army army;

    public ArmyMessage(Army army) {
        this.army = army;
    }

    public Army getStrength() {
        return army;
    }

    public Army getArmy() {
        return army;
    }

    public void setArmy(Army army) {
        this.army = army;
    }

}

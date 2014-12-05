package gossipico;

import java.io.Serializable;

public class ArmyStrengthMessage implements Serializable {
    private int strength;

    public ArmyStrengthMessage(int strength) {
        this.strength = strength;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }
}

package p2pbay.core;

import java.io.Serializable;

public class Bid extends DHTObject implements Serializable, Comparable<Object>{
    private static final long serialVersionUID = 6128016096756071380L;
    private String title;
    private float value;
    private String owner;

    public Bid(String title, String owner, float value) {
        super(title, DHTObjectType.BID);
        this.title = title;
        this.owner = owner;
        this.value = value;
    }

    public String getTitle() {
        return this.title;
    }
    
    public float getValue() {
        return this.value;
    }
    
    public String getOwner() {
        return this.owner;
    }

    public String getValueToString() {
        return getValue() + "€";
    }

    @Override
    public String toString() {
        return super.toString() + owner + ":" + value;
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof Bid) {
            float compareValue = this.getValue() - ((Bid)o).getValue();
            if (compareValue < 0)
                return -1;
            else if (compareValue > 0)
                return 1;
            else return 0;
        }
        return 0;
    }
}

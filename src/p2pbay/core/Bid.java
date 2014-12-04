package p2pbay.core;

import java.io.Serializable;

public class Bid extends DHTObject implements Serializable, Comparable{
    private static final long serialVersionUID = 6128016096756071380L;
    private String title;
    private float value;
    private String owner;
    private int position;
    private boolean invalid;
    
    public Bid(String title, User owner, float value) {
        super(title, DHTObjectType.BID);
        this.title = title;
        this.owner = owner.getUsername();
        this.value = value;
        this.position = 0;
        this.invalid = false;
    }

    public void setInvalid(boolean invalid) {
        this.invalid = invalid;
    }

    public boolean isInvalid() {
        return invalid;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
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
        return super.toString() + owner + ":" + value + ":" + position;
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof Bid) {
            return this.getPosition() - ((Bid)o).getPosition();
        }
        return 0;
    }
}

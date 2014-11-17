package core;

import java.io.Serializable;

public class Bid implements Serializable {
    private static final long serialVersionUID = 6128016096756071380L;
    private String title;
    private float value;
    private String owner;
    
    public Bid(String title, String owner, float value) {
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
}

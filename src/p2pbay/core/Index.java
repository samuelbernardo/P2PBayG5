package p2pbay.core;

import java.util.ArrayList;
import java.util.List;

public class Index {
    private String term;
    private List<String> items;
    
    public Index(String term, String title) {
        this.term = term;
        this.items = new ArrayList<String>();
        this.items.add(title);
    }
    
    public String getTerm() {
        return this.term;
    }
    
    public List<String> getItems() {
        return this.items;
    }
    
    public void addTitle(String title) {
        this.items.add(title);
    }
}

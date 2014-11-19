package p2pbay.core;

import java.io.Serializable;
import java.util.TreeSet;

public class Index implements Serializable {
    private static final long serialVersionUID = 6128016096756071380L;
    private String term;
    private TreeSet<String> titles;
    
    public Index(String term, String title) {
        this.term = term;
        this.titles = new TreeSet<String>();
        this.titles.add(title);
    }
    
    public String getTerm() {
        return this.term;
    }
    
    public TreeSet<String> getTitles() {
        return this.titles;
    }
    
    public void addTitle(String title) {
        this.titles.add(title);
    }
    
    public void removeTitle(String title) {
        titles.remove(title);
    }
}

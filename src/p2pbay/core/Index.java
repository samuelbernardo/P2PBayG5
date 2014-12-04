package p2pbay.core;

import java.io.Serializable;
import java.util.TreeSet;

public class Index extends DHTObject implements Serializable {

    private static final long serialVersionUID = 6128016096756071380L;
    private String term;
    private TreeSet<String> titles;
    
    public Index(String term, String title) {
        super(term, DHTObjectType.INDEX);
        this.term = term;
        this.titles = new TreeSet<>();
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

    @Override
    public String toString() {
        return super.toString() + titles;
    }
}

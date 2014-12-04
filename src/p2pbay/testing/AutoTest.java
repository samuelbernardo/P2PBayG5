package p2pbay.testing;

import p2pbay.P2PBay;
import p2pbay.core.DHTObjectType;
import p2pbay.core.Index;
import p2pbay.core.Item;

public class AutoTest {
    public static void storeItems() {
        TitleGenerator titleGenerator = new TitleGenerator();
        for (int i = 0; i < 50; i++) {
            String title = titleGenerator.nextTitle();
            Item item = new Item("owner", title, "", (float) 1);
            P2PBay.P2PBAY.store(item);
        }
    }

//    public static void indexItems(String title) {
//        for(String term : title.split(" "))
//            indexTerm(term);
//    }
//
//    public void indexTerm(String term) {
//        String key = term;
//        Index index = (Index) P2PBay.P2PBAY.get(key, DHTObjectType.INDEX);
//
//        if (index == null)
//            index = new Index(key, title);
//        else
//            index.addTitle(title);
//
//        if(getClient().store(index))
//            System.out.println("Term:" + index.getTerm() + " actualizado");
//        else
//            System.err.println("Ocorreu um erro na insercao do indice...");
//    }
}

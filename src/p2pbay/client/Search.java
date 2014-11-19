package p2pbay.client;


import java.util.Iterator;
import java.util.Scanner;
import java.util.TreeSet;

import p2pbay.core.Index;
import p2pbay.server.TomP2PHandler;

public class Search {

    private TomP2PHandler tomp2p;
    //private List<String> booleanOperationsPermited;
    private String search;


    public Search(TomP2PHandler tomp2p, Scanner input) {
        this.tomp2p = tomp2p;
        //this.booleanOperationsPermited = new ArrayList<String>(Arrays.asList("AND", "OR", "NOT"));
        this.search = getBooleanSearch(input);
        analyse();
    }

    private String getBooleanSearch(Scanner input) {
        System.out.println("Pesquisa:");
        return input.nextLine();
    }

    private void analyse() {
        String[] splitSearch = this.search.split(" ");
        int nWords = splitSearch.length;
        TreeSet<String> previousResult = new TreeSet<String>();

        //if (isValid(expression)) {
        Index indexWithTerm1 = (Index) tomp2p.get(splitSearch[nWords-2]);
        TreeSet<String> titlesWithTerm1 = indexWithTerm1.getTitles();
        Index indexWithTerm2 = (Index) tomp2p.get(splitSearch[nWords-1]);
        TreeSet<String> titlesWithTerm2 = indexWithTerm2.getTitles();
        previousResult = analyse(splitSearch[nWords-3], titlesWithTerm1, titlesWithTerm2);
        nWords -= 3;
        while(nWords > 0) {
            indexWithTerm1 = (Index) tomp2p.get(splitSearch[nWords-1]);
            titlesWithTerm1 = indexWithTerm1.getTitles();
            previousResult = analyse(splitSearch[nWords-2], titlesWithTerm1, previousResult);
            nWords -= 2;
        }

        Iterator<String> iterator = previousResult.iterator();
        System.out.print("Resultados da pesquisa:\n");
        while (iterator.hasNext()) {
            System.out.print(iterator.next() + "\n");
        }
    }

    /*private boolean isValid(String[] expression) {
    if (booleanOperationsPermited.contains(expression[0])
            || !booleanOperationsPermited.contains(expression[1])
            || !booleanOperationsPermited.contains(expression[2]))
        return true;
    else return false;
}*/

    private TreeSet<String> analyse(String operator, TreeSet<String> titlesWithTerm1, TreeSet<String> titlesWithTerm2) {
        TreeSet<String> titles = new TreeSet<String>();

        switch (operator) {
            case "AND":
                titles.addAll(titlesWithTerm1);
                titles.retainAll(titlesWithTerm2);
                break;
            case "OR":
                titles.addAll(titlesWithTerm1);
                titles.addAll(titlesWithTerm2);
                break;
            case "NOT":
                // removeAll
                break;
        }

        return titles;
    }
}

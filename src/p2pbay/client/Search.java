package p2pbay.client;

import java.lang.UnsupportedOperationException;
import java.util.Iterator;
import java.util.Scanner;
import java.util.TreeSet;

import p2pbay.core.Index;
import p2pbay.server.TomP2PHandler;

public class Search {

    private TomP2PHandler tomp2p;
    private String search;
    private String[] splitSearch;
    private int nWords;
    private TreeSet<String> previousResult;

    public Search(TomP2PHandler tomp2p, Scanner input) {
        this.tomp2p = tomp2p;
        this.search = getBooleanSearch(input);
    }
    
    public void execute() {
        try {
            doSearch();
            printResults();
        }
        catch(ArrayIndexOutOfBoundsException e) {
            System.out.println("A expressao nao esta bem formada, por favor tente novamente.");
        }
    }

    private String getBooleanSearch(Scanner input) {
        System.out.println("Pesquisa:");
        return input.nextLine();
    }

    private void doSearch() {

        this.splitSearch = this.search.split(" ");
        this.previousResult = new TreeSet<String>();
        this.nWords = splitSearch.length;

        // análise da primeira expressão mais à direita
        if(!splitSearch[nWords-2].equals("NOT")) { 
            this.previousResult = analyseWithoutNOT(nWords-3, 1);
            this.nWords -= 3;
        }
        else {
            this.previousResult = analyseWithNOT();
            this.nWords -= 4;
        }

        // análise das restantes expressões, se houver
        while(nWords > 0) {
            if(!splitSearch[nWords-2].equals("NOT")) {
                this.previousResult = analyseWithoutNOT(nWords-2, 2);
                nWords -= 2;
            }
            else {
                this.previousResult = analyseWithNOT();
                nWords -= 3;
            }
        }
    }



    // type = 1 significa que é a primeira análise da direita; type = 2 significa que é uma das análises mais à esquerda
    private TreeSet<String> analyseWithoutNOT(int booleanOperatorPosition, int type) {
        TreeSet<String> titles = new TreeSet<String>();
        Index indexWithTerm1;
        Index indexWithTerm2;
        TreeSet<String> titlesWithTerm1 = new TreeSet<String>();
        TreeSet<String> titlesWithTerm2 = new TreeSet<String>();;

        if(type==1) {
            indexWithTerm1 = (Index) tomp2p.get("index" + splitSearch[this.nWords-2]);
            indexWithTerm2 = (Index) tomp2p.get("index" + splitSearch[this.nWords-1]);
            if(indexWithTerm1 != null)
                titlesWithTerm1 = indexWithTerm1.getTitles();
            if(indexWithTerm2 != null)
                titlesWithTerm2 = indexWithTerm2.getTitles();
        }
        else {
            indexWithTerm1 = (Index) tomp2p.get("index" + splitSearch[this.nWords-1]);
            titlesWithTerm1 = indexWithTerm1.getTitles();
            titlesWithTerm2 = this.previousResult;
        }

        try {
            titles = doBooleanOperation(splitSearch[booleanOperatorPosition], titlesWithTerm1, titlesWithTerm2);
        } catch(UnsupportedOperationException e) {
            System.out.println(e.getMessage());
        }

        return titles;
    }

    private TreeSet<String> analyseWithNOT() {
        TreeSet<String> NOTresults = new TreeSet<String>();
        TreeSet<String> titles = new TreeSet<String>();
        Index indexWithTerm1 = (Index) tomp2p.get("index" + this.splitSearch[nWords-3]);
        TreeSet<String> titlesWithTerm1 = indexWithTerm1.getTitles();
        Index indexWithTerm2 = (Index) tomp2p.get("index" + this.splitSearch[nWords-1]);
        TreeSet<String> titlesWithTerm2 = indexWithTerm2.getTitles();
        NOTresults.addAll(titlesWithTerm1);
        NOTresults.removeAll(titlesWithTerm2);
        try {
            titles = doBooleanOperation(this.splitSearch[this.nWords-4], titlesWithTerm1, NOTresults);
        }
        catch(UnsupportedOperationException e) {
            System.out.println(e.getMessage());
        }
        return titles;
    }

    private TreeSet<String> doBooleanOperation(String operator, TreeSet<String> titlesWithTerm1, TreeSet<String> titlesWithTerm2) throws UnsupportedOperationException {
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
            default:
                throw new UnsupportedOperationException("ERRO! Keyword booleana invalida: " + operator);
        }
        return titles;
    }

    private void printResults() {
        if(!this.previousResult.isEmpty()) {
            Iterator<String> iterator = this.previousResult.iterator();
            System.out.print("\nResultados da pesquisa:\n");
            while (iterator.hasNext())
                System.out.print(iterator.next() + "\n");
        }
        else
            System.out.println("Sem resultados... Por favor tente outras keywords.");
    }
}

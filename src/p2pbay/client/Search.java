package p2pbay.client;

import java.lang.UnsupportedOperationException;
import java.util.TreeSet;

import p2pbay.client.user.UserInteraction;
import p2pbay.core.Index;

public class Search extends UserInteraction {

    private String search;
    private String[] splitSearch;
    private int nWords;
    private TreeSet<String> previousResult;

    public Search(Client client) {
        super(client);
    }

    private void doSearch() {

        this.splitSearch = this.search.split(" ");
        this.previousResult = new TreeSet<String>();
        this.nWords = splitSearch.length;

        // análise da primeira expressão mais à direita
        if(splitSearch[nWords-2].equals("NOT")){
            this.previousResult = analyseWithNOT(nWords-2, 1);
            this.nWords -= 4;
        }
        if(splitSearch[nWords-3].equals("NOT")){
            this.previousResult = analyseWithNOT(nWords-3, 1);
            this.nWords -= 4;
        }
        else { 
            this.previousResult = analyseWithoutNOT(nWords-3, 1);
            this.nWords -= 3;
        }

        // análise das restantes expressões, se houver
        while(nWords > 0) {
            if(splitSearch[nWords-2].equals("NOT")){
                this.previousResult = analyseWithNOT(nWords-2, 2);
                nWords -= 3;
            }
            else {
                this.previousResult = analyseWithoutNOT(nWords-2, 2);
                nWords -= 2;
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
            indexWithTerm1 = getClient().getIndex(splitSearch[this.nWords-2]);
            indexWithTerm2 = getClient().getIndex(splitSearch[this.nWords-1]);
            if(indexWithTerm1 != null)
                titlesWithTerm1 = indexWithTerm1.getTitles();
            if(indexWithTerm2 != null)
                titlesWithTerm2 = indexWithTerm2.getTitles();
        }
        else {
            indexWithTerm1 = getClient().getIndex(splitSearch[this.nWords - 1]);
            titlesWithTerm1 = indexWithTerm1.getTitles();
            titlesWithTerm2 = this.previousResult;
        }

        try {
            titles = doBooleanOperation(3, splitSearch[booleanOperatorPosition], titlesWithTerm1, titlesWithTerm2);
        } catch(UnsupportedOperationException e) {
            System.out.println(e.getMessage());
        }

        return titles;
    }

    private TreeSet<String> analyseWithNOT(int NOTposition, int type) {
        Index indexWithTerm1;
        Index indexWithTerm2;
        TreeSet<String> titlesWithTerm1;
        TreeSet<String> titlesWithTerm2;
        TreeSet<String> NOTtitles = new TreeSet<String>();
        TreeSet<String> titles = new TreeSet<String>();
        int firstTermPosition;
        
        if(type == 1) {
            
            if(NOTposition == nWords-2) {
                firstTermPosition = nWords-3;
            }
            else {
                firstTermPosition = nWords-2;
            }
            indexWithTerm1 = getClient().getIndex(splitSearch[firstTermPosition]);
            indexWithTerm2 = getClient().getIndex(splitSearch[nWords - 1]);
            titlesWithTerm1 = indexWithTerm1.getTitles();
            titlesWithTerm2 = indexWithTerm2.getTitles();

            NOTtitles.addAll(titlesWithTerm1);
            NOTtitles.removeAll(titlesWithTerm2);
            try {
                titles = doBooleanOperation(type, splitSearch[this.nWords-4], titlesWithTerm1, NOTtitles);
            }
            catch(UnsupportedOperationException e) {
                System.out.println(e.getMessage());
            }
        }
        else {
            if(NOTposition == nWords-2) {
                firstTermPosition = nWords-1;
                indexWithTerm1 = getClient().getIndex(splitSearch[firstTermPosition]);
                titlesWithTerm1 = indexWithTerm1.getTitles();
                titlesWithTerm2 = previousResult;
                NOTtitles.addAll(titlesWithTerm1);
                titles = doBooleanOperation(type, splitSearch[nWords-3], NOTtitles, titlesWithTerm2);
            }
            else {
                firstTermPosition = nWords-2;
                indexWithTerm1 = getClient().getIndex(splitSearch[firstTermPosition]);
                titlesWithTerm1 = indexWithTerm1.getTitles();
                titlesWithTerm2 = previousResult;
                NOTtitles.addAll(titlesWithTerm2);
                titles = doBooleanOperation(2, splitSearch[nWords-3], titlesWithTerm1, NOTtitles);
            }
        }
        return titles;
    }

    private TreeSet<String> doBooleanOperation(int type, String operator, TreeSet<String> titlesWithTerm1, TreeSet<String> titlesWithTerm2) throws UnsupportedOperationException {
        TreeSet<String> titles = new TreeSet<String>();
        if(type == 1) {
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
        }
        
        
        if(type == 3) {
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
        }
        return titles;
    }

    private void printResults() {
        if(previousResult.isEmpty()) {
            System.out.println("Sem resultados... Por favor tente outras keywords.");
            return;
        }
        System.out.println("\nResultados da pesquisa:");
        for (String title : previousResult) {
            System.out.println(" - " + title);
        }
    }

    @Override
    public void getInfo() {
        System.out.print("Pesquisa:");
        search = getClient().getInput();

        try {
            doSearch();
            printResults();
        }
        catch(ArrayIndexOutOfBoundsException e) {
            System.out.println("A expressao nao esta bem formada, por favor tente novamente.");
        }
    }

    @Override
    public void storeObjects() {
    }
}

package p2pbay.client;

import java.util.TreeSet;

import p2pbay.client.user.UserInteraction;
import p2pbay.core.Index;

public class Searchv2 extends UserInteraction {
    TreeSet<String> searchResult;
    TreeSet<String> previousResult; 
    String booleanOperator;
    TreeSet<String> titlesWithTerm1;
    TreeSet<String> titlesWithTerm2;
    TreeSet<String> NOTtitles;
    String search;
    String[] splitSearch;
    int nWords;
    boolean usePreviousResult;

    public Searchv2(Client client) {
        super(client);
        searchResult = new TreeSet<String>();
        previousResult = new TreeSet<String>();
        booleanOperator = null;
        titlesWithTerm1 = new TreeSet<String>();
        titlesWithTerm2 = new TreeSet<String>();
        NOTtitles = new TreeSet<String>();
        usePreviousResult = false;
    }

    // arranjar solucao para discordancia do nome com o que faz
    public void getInfo() {
        System.out.print(SysStrings.SEARCH);
        search = getClient().getInput();
        splitSearch = search.split(" ");
        nWords = splitSearch.length;

        try {
            doSearch();
            printResults();
        } catch (UnsupportedOperationException e) {
            System.out.println(e.getMessage());
        }
    }

    private void doSearch() {
        if (nWords == 0) return;
        if (nWords == 1) {
            doSimpleSearch();
        }
        if (nWords == 2 && usePreviousResult) {
            getTitles("withoutNOT");
            nWords -= 2;
        }
        else if (nWords == 3) {
            getTitles("withoutNOT");
            doSimpleBooleanSearchWithoutNot();
        }
        else if (nWords == 4) {
            if(!usePreviousResult) {
                getTitles("withNOT");
                doSimpleBooleanSearchWithNot();
            }
            else {
                getTitles("withoutNOT");
                doSimpleBooleanSearchWithoutNot();
                nWords -= 2;
                printResults();
                doSearch();
            }
        }
        else if (nWords > 4) {
            int NOTposition = findNOTposition();
            if (NOTposition == -1) {
                getTitles("withoutNOT");
                doSimpleBooleanSearchWithoutNot();
                usePreviousResult = true;
                nWords -= 3;
                printResults(); // apagar
                doSearch();
            }
            else {
                getTitles("withNOT");
            }
        }

        else
            System.out.println(SysStrings.SEARCH_FAILED);
    }

    private void getTitles(String hasNOT) {
        Index index;
        switch(hasNOT) {
            case "withoutNOT":
                index = getClient().getIndex(splitSearch[nWords-1]);
                if(index != null) {
                    titlesWithTerm1 = index.getTitles();
                }
                if(!usePreviousResult){
                    index = getClient().getIndex(splitSearch[nWords-2]);
                    if(index != null) {
                        titlesWithTerm2 = index.getTitles();
                    }
                    booleanOperator = splitSearch[nWords-3];
                }
                else
                    titlesWithTerm2 = previousResult;
                break;
            case "withNOT":
                int NOTposition = findNOTposition();
                NOTtitles = new TreeSet<String>();
                if(NOTposition == nWords-3) {
                    index = getClient().getIndex(splitSearch[nWords-1]);
                    if(index != null) {
                        previousResult = index.getTitles();
                    }
                    index = getClient().getIndex(splitSearch[nWords-2]);
                    if(index != null) {
                        NOTtitles = index.getTitles();
                    }
                }
                else if(NOTposition == nWords-2) {
                    index = getClient().getIndex(splitSearch[nWords-3]);
                    if(index != null) {
                        previousResult = index.getTitles();
                    }
                    index = getClient().getIndex(splitSearch[nWords-1]);
                    if(index != null) {
                        NOTtitles = index.getTitles();
                    }
                }
                booleanOperator = splitSearch[nWords-4];
                break;
        }
    }

    private void doSimpleSearch() {
        Index index = getClient().getIndex(splitSearch[nWords-1]);
        if(index != null)
            searchResult = index.getTitles();
    }

    private int findNOTposition() {
        int NOTposition = -1;
        if (splitSearch[nWords-2].equals("NOT")) {
            NOTposition = nWords-2;
        }
        else if (splitSearch[nWords-3].equals("NOT"))
            NOTposition = nWords-3;
        return NOTposition;
    }

    private void doSimpleBooleanSearchWithoutNot() throws UnsupportedOperationException {
        switch(booleanOperator) {
            case "AND":
                searchResult.addAll(titlesWithTerm1);
                searchResult.retainAll(titlesWithTerm2);
                previousResult = searchResult;
                break;
            case "OR":
                searchResult.addAll(titlesWithTerm1);
                searchResult.addAll(titlesWithTerm2);
                previousResult = searchResult;
                break;
            default:
                throw new UnsupportedOperationException(SysStrings.SEARCH_INVALIDOPERATOR + booleanOperator);
        }
    }

    private void doSimpleBooleanSearchWithNot() throws UnsupportedOperationException {
        switch(booleanOperator) {
            case "AND":
                searchResult.addAll(previousResult);
                searchResult.removeAll(NOTtitles);
                break;
            case "OR":
                throw new UnsupportedOperationException(SysStrings.SEARCH_IMPOSSIBLE);
            default:
                throw new UnsupportedOperationException(SysStrings.SEARCH_INVALIDOPERATOR + booleanOperator);
        }
    }

    private void printResults() {
        if(searchResult.isEmpty()) {
            System.out.println(SysStrings.SEARCH_NORESULTS);
            return;
        }
        System.out.println(SysStrings.SEARCH_RESULTS);
        for (String title : searchResult) {
            System.out.println(" - " + title);
        }
    }

    @Override
    public void storeObjects() {
    }
}

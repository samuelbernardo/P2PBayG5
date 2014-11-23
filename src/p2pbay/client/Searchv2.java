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

    public Searchv2(Client client) {
        super(client);
        searchResult = new TreeSet<String>();
        previousResult = new TreeSet<String>();
        booleanOperator = null;
        titlesWithTerm1 = new TreeSet<String>();
        titlesWithTerm2 = new TreeSet<String>();
        NOTtitles = new TreeSet<String>();
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
        // pesquisa com uma palavra
        if (nWords == 1) {
            doSimpleSearch();
        }
        // pesquisa com AND ou OR (sem NOT)
        else if (nWords == 3) {
            getPartialTitles("withoutNOT");
            doSimpleBooleanSearchWithoutNot();
        }
        else if (nWords == 4) {
            getPartialTitles("withNOT");
            doSimpleBooleanSearchWithNot();
        }

        else
            System.out.println(SysStrings.SEARCH_FAILED);
    }

    private void getPartialTitles(String hasNOT) {
        Index index;
        switch(hasNOT) {
            case "withoutNOT":
                index = getClient().getIndex(splitSearch[nWords-1]);
                if(index != null) {
                    titlesWithTerm1 = index.getTitles();
                }
                index = getClient().getIndex(splitSearch[nWords-2]);
                if(index != null) {
                    titlesWithTerm2 = index.getTitles();
                }
                booleanOperator = splitSearch[nWords-3];
                break;
            case "withNOT":
                int NOTposition = findNOTposition();
                NOTtitles = new TreeSet<String>();
                if(NOTposition == nWords-3) {
                    index = getClient().getIndex(splitSearch[nWords-1]);
                    if (index != null)
                        previousResult = index.getTitles();
                    index = getClient().getIndex(splitSearch[nWords-2]);
                    if (index != null)
                        NOTtitles = index.getTitles();
                }
                else if(NOTposition == nWords-2) {
                    index = getClient().getIndex(splitSearch[nWords-3]);
                    if (index != null)
                        previousResult = index.getTitles();
                    index = getClient().getIndex(splitSearch[nWords-1]);
                    if (index != null)
                        NOTtitles = index.getTitles();
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
        else
            throw new UnsupportedOperationException(SysStrings.SEARCH_FAILED);
        return NOTposition;
    }

    private void doSimpleBooleanSearchWithoutNot() throws UnsupportedOperationException {
        switch(booleanOperator) {
            case "AND":
                searchResult.addAll(titlesWithTerm1);
                searchResult.retainAll(titlesWithTerm2);
                break;
            case "OR":
                searchResult.addAll(titlesWithTerm1);
                searchResult.addAll(titlesWithTerm2);
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

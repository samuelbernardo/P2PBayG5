package p2pbay.client;

import java.util.*;

import p2pbay.client.user.UserInteraction;
import p2pbay.core.Index;

public class Search extends UserInteraction{
    TreeSet<String> previousResult; 
    String booleanOperator;
    TreeSet<String> titlesWithTerm1;
    TreeSet<String> titlesWithTerm2;
    TreeSet<String> NOTtitles;
    String search;
    String[] splitSearch;
    int nWords;

    private HashMap<String, Index> indexes;

    public Search(Client client) {
        super(client);
        previousResult = new TreeSet<>();
        booleanOperator = null;
        titlesWithTerm1 = new TreeSet<>();
        titlesWithTerm2 = new TreeSet<>();
        NOTtitles = new TreeSet<>();
        indexes = new HashMap<>();
    }

    private boolean isOperator(String term) {
        return term.equals("OR") || term.equals("AND") || term.equals("NOT");
    }


    private void getAllIndex() {
        HashSet<String> terms = new HashSet<>();

        //Verificar quais os termos que sao para procurar
        for (String searchTerm : splitSearch) {
            if (!isOperator(searchTerm)) {
                terms.add(searchTerm);
            }
        }

        //Iniciar a pesquisa na dht
        for (String term : terms) {
            indexes.put(term, getClient().getIndex(term));
        }
        //Wait;;
    }

    private Index getIndex(String term) {
        return indexes.get(term);
    }

    // arranjar solucao para discordancia do nome com o que faz
    public void getInfo() {
        System.out.print(SysStrings.SEARCH);
        search = getClient().readInput();
        splitSearch = search.split(" ");
        getAllIndex();
        nWords = splitSearch.length;
        
        TreeSet<String> searchResult = new TreeSet<String>();
        if (nWords <= 4) {
            try {
                searchResult = doSearch();
                printResults(searchResult);
            } catch (UnsupportedOperationException e) {
                System.out.println(e.getMessage());
            }
        }
        else {
            try {
                searchResult = doComplexSearch();
                printResults(searchResult);
            } catch (UnsupportedOperationException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private TreeSet<String> doSearch() {
        TreeSet<String> searchResult = new TreeSet<String>();

        // pesquisa com uma palavra
        if (nWords == 1)
            searchResult = doSimpleSearch();
        else if (nWords == 2 && splitSearch[nWords-2].equals("NOT"))
            throw new UnsupportedOperationException(SysStrings.SEARCH_IMPOSSIBLE);
        // pesquisa com AND ou OR (sem NOT)
        else if (nWords == 3) {
            getPartialTitles("withoutNOT");
            searchResult = doSimpleBooleanSearchWithoutNot();
        }
        else if (nWords == 4) {
            getPartialTitles("withNOT");
            searchResult = doSimpleBooleanSearchWithNot();
        }
        else
            System.out.println(SysStrings.SEARCH_FAILED);

        return searchResult;
    }

    private TreeSet<String> doComplexSearch() {
        TreeSet<String> searchResult = new TreeSet<String>();
        
        if (!hasNOT()) {
            getPartialTitles("withoutNOT");
            searchResult = doSimpleBooleanSearchWithoutNot();
            nWords -= 3;
        }
        else {
            getPartialTitles("withNOT");
            searchResult = doSimpleBooleanSearchWithNot();
            nWords -= 4;
        }
        while(nWords > 0) {
            if(!hasNOT()) {
                titlesWithTerm1 = doSimpleSearch();
                titlesWithTerm2 = searchResult;
                booleanOperator = splitSearch[nWords-2];
                searchResult = doSimpleBooleanSearchWithoutNot();
                nWords -= 2;
            }
            else {
                Index index = getIndex(splitSearch[nWords-1]);
                if(index != null) {
                    NOTtitles = index.getTitles();
                }
                previousResult = searchResult;
                searchResult = doSimpleBooleanSearchWithNot();
                nWords -= 3;
            }
        }
        return searchResult;
    }
    
    private void getPartialTitles(String hasNOT) {
        Index index;
        switch(hasNOT) {
            case "withoutNOT":
                index = getIndex(splitSearch[nWords-1]);
                
                if(index != null) {
                    titlesWithTerm1 = index.getTitles();
                }
                index = getIndex(splitSearch[nWords-2]);
                if(index != null) {
                    titlesWithTerm2 = index.getTitles();
                }
                booleanOperator = splitSearch[nWords-3];
                break;
            case "withNOT":
                int NOTposition = findNOTposition();
                NOTtitles = new TreeSet<String>();
                if(NOTposition == nWords-3) {
                    index = getIndex(splitSearch[nWords-1]);
                    if (index != null)
                        previousResult = index.getTitles();
                    index = getIndex(splitSearch[nWords-2]);
                    if (index != null)
                        NOTtitles = index.getTitles();
                }
                else if(NOTposition == nWords-2) {
                    index = getIndex(splitSearch[nWords-3]);
                    if (index != null)
                        previousResult = index.getTitles();
                    index = getIndex(splitSearch[nWords-1]);
                    if (index != null)
                        NOTtitles = index.getTitles();
                }
                booleanOperator = splitSearch[nWords-4];
                break;
        }
    }

    private TreeSet<String> doSimpleSearch() {
        TreeSet<String> searchResult = new TreeSet<String>();
        Index index = getClient().getIndex(splitSearch[nWords-1]);
        if(index != null) {
            searchResult = index.getTitles();
        }
        return searchResult;
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

    private boolean hasNOT() {
        boolean hasNOT = false;
        if (splitSearch[nWords-2].equals("NOT")) {
            hasNOT = true;
        }
        else if (nWords >= 3) {
            if (splitSearch[nWords-3].equals("NOT")) {
                hasNOT = true;
            }
        }
        else
            hasNOT = false;
        return hasNOT;
    }
    
    private TreeSet<String> doSimpleBooleanSearchWithoutNot() throws UnsupportedOperationException {
        TreeSet<String> searchResult = new TreeSet<String>();
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
        return searchResult;
    }

    private TreeSet<String> doSimpleBooleanSearchWithNot() throws UnsupportedOperationException {
        TreeSet<String> searchResult = new TreeSet<String>();
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
        return searchResult;
    }

    private void printResults(TreeSet<String> searchResult) {
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
    public void storeObjects() {}
}

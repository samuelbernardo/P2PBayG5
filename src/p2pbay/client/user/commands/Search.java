package p2pbay.client.user.commands;

import java.util.*;

import p2pbay.client.Client;
import p2pbay.client.SysStrings;
import p2pbay.client.user.UserInteraction;
import p2pbay.core.Index;

public class Search extends UserInteraction{
    TreeSet<String> previousResult;
    String word;
    TreeSet<String> titlesWithTerm1;
    TreeSet<String> titlesWithTerm2;
    TreeSet<String> NOTtitles;
    String search;
    String[] splitSearch;
    int nWords;
    int position = 0;

    private HashMap<String, Index> indexes;
    private TreeSet<String> searchResult;

    public Search(Client client) {
        super(client);
        previousResult = new TreeSet<>();
        word = null;
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
        try {
            searchResult = doSearch(true);
            if (splitSearch[position+1] == null)
                printResults();
            else throw new UnsupportedOperationException(SysStrings.SEARCH_FAILED);
        } catch (UnsupportedOperationException e) {
            System.out.println(e.getMessage());
        }
    }

    private TreeSet<String> doSearch(boolean invalidNOT ) throws UnsupportedOperationException {
        word = splitSearch[position];

        if (word == null)
            throw new UnsupportedOperationException(SysStrings.SEARCH_FAILED);

        if (invalidNOT && word.equals("NOT"))
            throw new UnsupportedOperationException(SysStrings.SEARCH_IMPOSSIBLE);

        if (isOperator(word)) {
            return doOperation(word);
        }

        Index index = getIndex(word);
        if (index == null)
             return new TreeSet<>();

        return index.getTitles();
    }

    private TreeSet<String> doOperation(String operator) {
        TreeSet<String> result = new TreeSet<>();
        position += 1;
        switch (operator) {
            case "AND":
                result.addAll(doSearch(false));
                position++;
                result.retainAll(doSearch(false));
                break;
            case "OR":
                result.addAll(doSearch(true));
                position++;
                result.addAll(doSearch(true));
                break;
            case "NOT":
                TreeSet<String> notWords = doSearch(false);
                for (Index index : indexes.values()) {
                    result.addAll(index.getTitles());
                }
                result.removeAll(notWords);
        }
        return result;
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
    public void doOperation() {}
}

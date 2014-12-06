package p2pbay.client.user.commands;

import java.util.*;

import p2pbay.client.Client;
import p2pbay.client.SysStrings;
import p2pbay.client.user.UserInteraction;
import p2pbay.core.Index;
import p2pbay.core.listener.GetListener;

/**
 * Comando para efectuar uma pesquisa sobre os items presentes na DHT e registados no indice invertido
 */
public class Search extends UserInteraction{
    private String word;
    private int position;
    private HashMap<String, Index> indexes;
    private String search;
    private String[] splitSearch;
    private TreeSet<String> searchResult;

    public Search(Client client) {
        super(client);
        word = null;
        position = 0;
        indexes = new HashMap<>();
    }

    @Override
    public void getInfo() {
        search = readInput(SysStrings.SEARCH);
        splitSearch = search.split(" ");
        getAllIndex();
    }
    
    @Override
    public void doOperation() {
        try {
            searchResult = doSearch(true);
            if (splitSearch[position+1] == null)
                printResults();
            else throw new UnsupportedOperationException(SysStrings.SEARCH_FAILED);
        } catch (UnsupportedOperationException e) {
            System.out.println(e.getMessage());
        } catch (ArrayIndexOutOfBoundsException searchComplete) {
            printResults();
        }
    }
    
    private void getAllIndex() {
        HashSet<String> terms = new HashSet<>();
        List<GetListener> listeners = new ArrayList<>();

        //Verificar quais os termos que sao para procurar
        for (String searchTerm : splitSearch) {
            if (!isOperator(searchTerm)) {
                if (terms.add(searchTerm)) { // Se o termo for adicionado ao Set inicia a pesquisa do termo
                    GetListener getListener = new GetListener(searchTerm);
                    listeners.add(getListener);
                    getClient().getIndex(getListener);
                    System.out.println("Searching for " + getListener.getKey());
                }
            }
        }

        // Obtem resultados das pesquisas
        for (GetListener listener : listeners) {
            indexes.put(listener.getKey(), (Index) listener.getObject());
            System.out.println("Got results for " + listener.getKey());
        }
        //Wait;;
    }
    
    private boolean isOperator(String term) {
        return term.equals("OR") || term.equals("AND") || term.equals("NOT");
    }
    
    private TreeSet<String> doSearch(boolean invalidNOT ) throws UnsupportedOperationException {
        word = splitSearch[position];

        if (word == null)
            throw new UnsupportedOperationException(SysStrings.SEARCH_FAILED);

        if (invalidNOT && word.equals("NOT"))
            throw new UnsupportedOperationException(SysStrings.SEARCH_IMPOSSIBLE);

        if (isOperator(word)) {
            return doBooleanOperation(word);
        }

        Index index = getIndex(word);
        if (index == null)
             return new TreeSet<>();

        return index.getTitles();
    }

    private Index getIndex(String term) {
        return indexes.get(term);
    }
    
    private TreeSet<String> doBooleanOperation(String operator) {
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
}

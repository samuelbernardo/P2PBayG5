package p2pbay.server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Classe para ligacao a rede p2pbay
 * Iniciar com o merodo loadConfig() para ler o ficheiro de nos
 */
public class P2PBayBootstrap {
    private static String configFile = "bootstrap.txt"; //Ficheiro com lista de nós

    /**
     * Lista de nos
     */
    private ArrayList<Node> nodes;

    public P2PBayBootstrap() {
        nodes = new ArrayList<>();
    }

    /**
     * Le e o ficheiro de configuracao
     * @return true se nao ocorreu nenhum erro, falso se nao ocoreu nenhum erro
     */
    public boolean loadConfig() {
        try (BufferedReader reader = new BufferedReader(new FileReader(configFile))) {
            String address;
            // Para cada linha adiciona um endereco a lista de dos
            while((address = reader.readLine()) != null) {
                nodes.add(new Node(address));
            }

            // Baralha a lista de nos, apenas para nao estar
            // sempre a tentar ligar aos mesmos nos
            Collections.shuffle(nodes);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @return Lista de nos
     */
    public ArrayList<Node> getNodes() {
        return nodes;
    }

    public void addLocal(int port) {
        nodes.add(new Node("localhost", port));
    }
}


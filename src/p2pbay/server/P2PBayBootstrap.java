package p2pbay.server;

import p2pbay.server.peer.Node;

import java.io.*;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

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

    public boolean loadLocalPort() {
        Scanner in = new Scanner(System.in);
        int port = 0;
        try {
            System.out.print("port:");
            port = Integer.parseInt(in.nextLine());
            nodes.add(new Node("localhost", port));
        } catch (NumberFormatException e) {
            return false;
        }
        if(port < 1000)
            return false;
        return true;
    }
}


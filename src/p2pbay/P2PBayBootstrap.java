package p2pbay;

import java.io.*;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.ArrayList;

/**
 * Classe para ligacao a rede p2pbay
 * Iniciar com o merodo loadConfig() para ler o ficheiro de nos
 */
public class P2PBayBootstrap {
    private static String configFile = "bootstrap.txt"; //Ficheiro com lista de nós

    /**
     * Lista de nos
     */
    private ArrayList<InetAddress> nodes;

    public P2PBayBootstrap() {
        nodes = new ArrayList<>();
    }

    /**
     * Le e o ficheiro de configuracao
     * @return
     */
    public boolean loadConfig() {
        try (BufferedReader reader = new BufferedReader(new FileReader(configFile))) {
            String line;
            while((line = reader.readLine()) != null) {
                nodes.add(Inet4Address.getByName(line));
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    public ArrayList<InetAddress> getNodes() {
        return nodes;
    }
}



package gossipico;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.tomp2p.peers.PeerAddress;
import p2pbay.server.TomP2PHandler;
import p2pbay.server.peer.Node;


/**
 * Classe che rappresenta un inizializzatore per il protocollo COUNT-BEACON.
 * Imposta un ID a tutti i nodi in modo da poterlo utilizzare per le stampe di debug
 * e comprendere come si evolve la simulazione
 * 
 * Questa classe inoltre inizializza il valore di forza di ogni esercito in modo che sia random perfetto
 * 
 * @author Nicola Corti
 */
public class CountBeaconInitializer extends Thread {

    private TomP2PHandler handler;
    
    /** Lista di interi distinti per assegnare i valori di forza */
    private List<Integer> ints;

    private Random random;


    /**
     *
     * @param handler
     */
    public CountBeaconInitializer(TomP2PHandler handler) {
        ints = new ArrayList<>();
        for (int j = 0; j < handler.getNetworkSize(); j++){
        	ints.add(j);
        }

        this.random = new Random();
    }

    @Override
    public void run() {
        try {
            Thread.sleep(10000 + random.nextInt(20000));
        } catch (InterruptedException ignore) {}

        while (handler.isRunning()) {
            for (PeerAddress node : handler.getNeighbors()) {
                int j = (int) (Math.random() * ints.size());
                if (j >= 0 && j < ints.size()){
                    // node.army.strength
                    handler.sendArmyStrength(new ArmyStrengthMessage(ints.get(j)), node);
                    ints.remove(j);
                } else {
                    // node.army.strength
                    handler.sendArmyStrength(new ArmyStrengthMessage(ints.get(0)), node);
                    ints.remove(0);
                }
            }
        }
    }

}

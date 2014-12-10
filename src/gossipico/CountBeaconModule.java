package gossipico;

import net.tomp2p.peers.PeerAddress;
import p2pbay.server.TomP2PHandler;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe che rappresenta il modulo che implementa l'algoritmo COUNT-BEACON,
 * la classe implementa CDProtocol e puo' essere usata all'interno del simulatore Peersim.
 * 
 * Oltre ad eseguire la computazione di COUNT, con una probabilita' p = 0.50 verra' effettuata
 * la computazione BEACON fra due nodi casuali
 * 
 * @author Nicola Corti
 */
public class CountBeaconModule extends CountModule implements Serializable {

	/** Esercito del nodo */
	protected Army army;
	
	/** Lista dei nodi che si sono disconnessi 
	 *  Utile per non notificare due volte la disconessione
	 */
	protected List<PeerAddress> disconnected;


	/**
	 *
	 * @param node
	 */
	public CountBeaconModule(TomP2PHandler node) {
		super(node);
		army = new Army(node);
		disconnected = new ArrayList<>();

		node.setCountBeaconModule(this);
	}

	public CountBeaconModule(TomP2PHandler node, Army army, Message waiting, Message received, int state_value, int user_state_value, int item_state_value, int state_freshness, int init_value, int user_init_value, int item_init_value, List<PeerAddress> disconnected) {
		super(node, waiting, received, state_value, user_state_value, item_state_value, state_freshness, init_value, user_init_value, item_init_value);

		this.army = army;
		this.disconnected = disconnected;
	}


	/* (non-Javadoc)
	 * @see it.ncorti.p2p.CountModule#
	 * 	nextCycle(peersim.core.Node, int)
	 */
	@Override
	public void nextCycle(TomP2PHandler node) {
		super.nextCycle(node);
		
		// Se uno dei vicini e' morto, resuscito l'esercito
		/*if (!checkNeighboor(link)){
			this.army.revive(this);
			this.resetCount();
		}*/
				
		// Con probabilita' p = 0.50 faccio una computazione BEACON fra due nodi 
		if (Math.random() > 0.50){

			PeerAddress next = null;
			int count = node.getNeighbors().size();
			
			// Itero fin quando non trovo un nodo up
			while (next == null && count != 0){
				int index = (int) (Math.random()* node.getNeighbors().size());
				next = node.getNeighbors().get(index);
				count--;
			}
			
			if (count == 0 && next == null) return;

			node.sendCountBeaconModuleMessage(this, next);
		}
	}

	/**
	 * Funzione per controllare se tutti i nodi vicini sono sempre up
	 * 
	 * link Protocollo che rappresenta la rete
	 * true se sono sutti up, false altrimenti
	 */
	/*private boolean checkNeighboor(Linkable link) {
		
		for (int i = 0; i < link.degree(); i++){
			Node e = link.getNeighbor(i);
			if (!e.isUp() && !disconnected.contains(e)){
				disconnected.add(e);
				return false;
			}
		}
		return true;
	}*/

	
	/* (non-Javadoc)
	 * @see it.ncorti.p2p.CountModule#
	 *	updateMessage(it.ncorti.p2p.CountModule)
	 */
	@Override
	protected boolean updateMessage(CountModule sender) {
		
		// Scarta i messaggi che non sono
		if (sender instanceof CountBeaconModule){
			CountBeaconModule send = (CountBeaconModule) sender;
			if (send.army.isSameArmy(this.army)){
				return super.updateMessage(sender);
			}
		}
		return false;
	}


	/* (non-Javadoc)
	 * @see it.ncorti.p2p.CountModule#getNeighbor(peersim.core.Node, int)
	 */
	@Override
	protected PeerAddress getNeighbor(TomP2PHandler node) {
		// Ritorna sempre il vicino piu' prossimo al beacon, tranne se lui stesso e' il beacon
		
		if (waiting.IC() && !this.isBeacon())
			return this.army.nexthop;
		return super.getNeighbor(node);
	}

	
	/**
	 * Funzione che ritorna true se il nodo in questione e' il beacon dell'esercito.
	 * 
	 * @return true se il nodo e' il beacon, false altrimenti
	 */
	private boolean isBeacon() {
		return (army.beacon.equals(this));
	}

	/* (non-Javadoc)
	 * @see it.ncorti.p2p.CountModule#toString()
	 */
	@Override
	public String toString(){
		return super.toString() + "\t | Beacon : " + army;
	}
	
}

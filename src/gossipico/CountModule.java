package gossipico;

import net.tomp2p.peers.PeerAddress;
import p2pbay.server.TomP2PHandler;
import p2pbay.server.peer.Node;

import java.io.Serializable;

/**
 * Classe che rappresenta il modulo che implementa l'algoritmo COUNT,
 * la classe implementa CDProtocol e puo' essere usata all'interno del simulatore Peersim.
 * 
 * Ad ogni ciclo ogni nodo contattera' in modo casuale un altro nodo e verra' effettuato lo scambio
 * delle informazioni fra i due nodi
 * 
 * @author Nicola Corti
 */
public class CountModule implements Serializable {

	/** Messaggio in attesa di essere inviato */
	protected Message waiting;
	/** Messaggio appena ricevuto */
	protected Message received;

	/** Variabile di stato: valore attuale */
	protected int state_value;
	/** Variabile di stato: freschezza attuale */
	protected int state_freshness;
		
	/** Valore iniziale del calcolo */
	protected int init_value;

	/** Valores para calculo de parametros */
	protected int user_init_value;
	protected int item_init_value;
	protected int user_state_value;
	protected int item_state_value;

	public void addUser() {
		user_state_value++;
	}

	public void addItem() {
		item_state_value++;
	}

	public int getUser_value() {
		return user_state_value;
	}

	public int getItem_value() {
		return item_state_value;
	}

	/** Node em que se baseia */
	protected TomP2PHandler node;

	public TomP2PHandler getNode() {
		return node;
	}

	public void setNode(TomP2PHandler node) {
		this.node = node;
	}

	public Message getWaiting() {
		return waiting;
	}

	public void setWaiting(Message waiting) {
		this.waiting = waiting;
	}


	/**
	 *
	 * @param node
	 */
	public CountModule(TomP2PHandler node) {

		this.node = node;
		node.setCountModule(this);
	
		init_value = 1;
		user_init_value = item_init_value = 0;
		user_state_value = item_state_value = 0;
		state_value = init_value;
		state_freshness = 1;

		waiting = new Message();
		received = new Message();
		
		/*if (func.contentEquals("count") || func.contentEquals("sum")){
			waiting = new Message();
			received = new Message();
		} else if (func.contentEquals("min")){
			waiting = new MinMessage(init_value);
			received = new MinMessage(init_value);
		} else if (func.contentEquals("max")){
			waiting = new MaxMessage(init_value);
			received = new MaxMessage(init_value);
		} */
	}

	public CountModule(TomP2PHandler node, Message waiting, Message received, int state_value, int user_state_value, int item_state_value, int state_freshness, int init_value, int user_init_value, int item_init_value) {
		this.node = node;
		this.waiting = waiting;
		this.received = received;
		this.state_value = state_value;
		this.user_state_value = user_state_value;
		this.item_state_value = item_state_value;
		this.state_freshness = state_freshness;
		this.init_value = init_value;
		this.user_init_value = user_init_value;
		this.item_init_value = item_init_value;
	}

	/* (non-Javadoc)
	 * @see peersim.cdsim.CDProtocol#nextCycle(peersim.core.Node, int)
	 */
	public void nextCycle(TomP2PHandler node) {
		
		/* Funzione invocata ad ogni ciclo della simulazione
		 * 1) Contatta un nodo vicino
		 * 2) Deposita il proprio messaggio in attesa
		 * 3) Invoca l'aggiornamento del messaggio sul vicino
		 * 4) Genera un messaggio di IS partendo dallo stato
		 */
		
		PeerAddress next = this.getNeighbor(node);
		if (next == null) return;

		node.sendCountModule(this, next);
	}
	
	/**
	 * Funzione per aggiornare il proprio messaggio in attesa.
	 * Si aggiorna il messaggio cercando di mantenere l'informazione contenuta nel messaggio IC 
	 * (scartando quindi eventuali messaggi IS) ed effettuando un merge quando entrambi i messaggi
	 * sono IC
	 * 
	 * @param sender Il riferimento al mittente che ha depositato il messaggio presso il nodo
	 * @return true se l'update e' andato a buon fine, false altrimenti.
	 */
	protected boolean updateMessage(CountModule sender) {
		
		if (received.IS() && waiting.IS() && received.isFresherThen(waiting)){
			waiting.update(received);
		} else if (received.IC() && waiting.IS()){
			waiting.update(received);
		} else if (received.IC() && waiting.IC()){
			waiting.merge(received);
		}
		
		// Aggiorna le variabili di stato
		if (waiting.isFresherThen(state_freshness)){
			state_freshness = waiting.getFreshness();
			state_value = waiting.getValue();
			if (user_state_value > user_init_value) {
				user_state_value = (user_state_value - user_init_value) + waiting.getUser_value();
				user_init_value = user_state_value;
			}
			if (item_state_value > item_init_value) {
				item_state_value = (item_state_value - item_init_value) + waiting.getItem_value();
				item_init_value = item_state_value;
			}
		}
		return true;
	}
	
	/**
	 * Funzione utilizzata dagli inizializzatori per impostare il valore iniziale
	 * del messaggio in attesa
	 * 
	 * @param val Valore iniziale
	 */
	protected void initValue(int val){
		this.waiting.value = val;
		this.init_value = val;
	}

	public int getValue() {
		return this.state_value;
	}

	/**
	 *
	 * @param node
	 * @return
	 */
	protected PeerAddress getNeighbor(TomP2PHandler node){
		
		PeerAddress next = null;
		int numberNeighbors = node.getNeighbors().size();

		if (numberNeighbors == 0)
			return null;
		int index = (int) (Math.random() * numberNeighbors);
		
		next = node.getNeighbors().get(index);
				
		return next;
	}

	/**
	 * Funzione che fa ripartire il conteggio di un nodo dal proprio valore iniziale
	 */
	protected void resetCount(){
		this.state_value = init_value;
		this.state_freshness = 1;
		this.waiting.update(init_value, 1, 1);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() { return ("Node " + this.getPeerID() + " \t Count: r = " + this.received + " w = " + this.waiting + " \t state v = " + state_value + " f = " + state_freshness + " \t"); }

	private String getPeerID() {
		return String.valueOf(node.getPeer().getPeerID());
	}

}

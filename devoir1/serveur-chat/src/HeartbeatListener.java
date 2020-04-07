import java.util.LinkedList;
import java.util.Queue;

import exception.PanneClientException;
/**
 * Un thead qui permet de recevoir tous des heartbeatMessage du client et les traiter
 * 
 */
public class HeartbeatListener extends Thread {
	//Grâce à l'avantage "First in first out" de Queue
	private static Queue<String> hbMsgList = new LinkedList<>();
	private Boolean closed = false;
	private long timeout = 10 * 1000;
	final private String clientName;

	/**
	 * thread heartbeat doit être terminé 
	 * 
	 */
	public Boolean didClientDie() {
		return this.closed;
	}

	public HeartbeatListener(Queue<String> hbMsgList, Boolean closed, String clientName) {
		this.hbMsgList = hbMsgList;
		this.closed = closed;
		this.clientName = clientName;
	}

	public void setHBListenrClosed(Boolean newClosed) {
		this.closed = newClosed;
	}

	public void checkClientAlive() throws PanneClientException {
		// Si la queue de HBMsg n'est pas vide -> cette client est active
		if (hbMsgList.peek() != null) {
			System.out.println("HBListener:\t" + hbMsgList.remove() + " is alive.");

		} else {
			try {
				Thread.sleep(timeout);
				if (hbMsgList.peek() == null) {
					this.closed = true;
					throw new PanneClientException("Le client" + this.clientName + " est en panne.");
				}
			} catch (Exception e) {
				throw new PanneClientException("Le client " + this.clientName + " est en panne.");
				// TODO: handle exception
			}
		}
	}

	public void run() {
		while (!this.closed) {
			try {
				checkClientAlive();
			} catch (Exception e) {
				System.out.println("Error: " + e.getMessage());
			}
		}
	}
}

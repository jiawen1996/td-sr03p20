package thread;
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
	private long timeout = 8 * 1000;
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
		
		try {
			if (hbMsgList.peek() != null) {
				System.out.println("HBListener:\t" + hbMsgList.remove() + " is alive.");
				Thread.sleep(6 * 1000);

			} else {
				Thread.sleep(timeout);
				hbMsgList.element();
			}

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("coucou");
			throw new PanneClientException("Le client \"" + this.clientName + "\" est en panne.");
		}
		
	}

	public void run() {
		while (!this.closed) {
			try {
				checkClientAlive();
			} catch (Exception e) {
				System.out.println("Error: " + e.getMessage());
				this.setHBListenrClosed(true);
			}
		}
	}
}

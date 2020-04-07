package thread;

import java.util.LinkedList;
import java.util.Queue;

import exception.PanneServeurException;

class HeartbeatListener extends Thread{
	private static Queue<String> hbMsgList = new LinkedList<>();
	private Boolean closed = false;
	private long timeout = 15 * 1000;

	public Boolean didClientDie() {
		return this.closed;
	}

	public HeartbeatListener(Queue<String> hbMsgList, Boolean closed) {
		this.hbMsgList = hbMsgList;
		this.closed = closed;
	}

	public void setHBListenrClosed(Boolean newClosed) {
		this.closed = newClosed;
	}

	public void checkServeurAlive() throws PanneServeurException {
		// Si la queue de HBMsg n'est pas vide -> cette client est active
		if (hbMsgList.peek() != null) {
			hbMsgList.poll();
		} else {
			try {
				Thread.sleep(timeout);
				if (hbMsgList.peek() == null) {
					this.closed = true;
					throw new PanneServeurException("Le serveur est en panne.");
				}
			} catch (Exception e) {
				throw new PanneServeurException("Le serveur est en panne.");
				// TODO: handle exception
			}
		}
	}

	public void run() {
		while (!this.closed) {
			try {
				checkServeurAlive();
			} catch (Exception e) {
				System.out.println("Error: " + e.getMessage());
				System.exit(0);
			}
		}
	}
}

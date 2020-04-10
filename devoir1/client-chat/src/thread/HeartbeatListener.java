package thread;

import java.util.LinkedList;
import java.util.Queue;

import exception.PanneServeurException;

class HeartbeatListener extends Thread {
	private static Queue<String> hbMsgList = new LinkedList<>();
	private Boolean closed = false;
	private long timeout = 10 * 1000;

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

		try {
			if (hbMsgList.peek() != null) {
				hbMsgList.poll();
				Thread.sleep(6 * 1000);

			} else {
				Thread.sleep(timeout);
				hbMsgList.element();
			}

		} catch (Exception e) {
			// TODO: handle exception
			throw new PanneServeurException("Le serveur est en panne.");
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

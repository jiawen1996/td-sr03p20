import java.util.LinkedList;
import java.util.Queue;

public class HeartbeatListener extends Thread {
	private static Queue<String> hbMsgList = new LinkedList<>();
	private Boolean closed = false;
	private long timeout = 8 * 1000;

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

	public void checkClientAlive() {
		//Si la queue de HBMsg n'est pas vide -> cette client est active
		if (hbMsgList.peek() != null) {
			System.out.println("HBListener:\t" + hbMsgList.remove() + " is alive.");

		} else {
			try {
				Thread.sleep(timeout);
				if (hbMsgList.peek() == null) {
					System.out.println("Client died");
					
					this.closed = true;
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

	public void run() {
		while(!this.closed) {
			checkClientAlive();
		}
	}
}

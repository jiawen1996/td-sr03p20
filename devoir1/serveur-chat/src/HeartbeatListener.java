import java.util.LinkedList;
import java.util.Queue;

public class HeartbeatListener extends Thread {
	private static Queue<String> hbMsgList = new LinkedList<>();
	private Boolean closed = false;
	private long timeout = 10 * 1000;

	public HeartbeatListener(Queue<String> hbMsgList, Boolean closed) {
		this.hbMsgList = hbMsgList;
		this.closed = closed;
	}

	public void setClosed(Boolean newClosed) {
		this.closed = newClosed;
	}

	public void checkClientAlive() {
		if (hbMsgList.peek() != null) {
			System.out.println("HBListener:\t" + hbMsgList.remove() + " is alive.");

		} else {
			try {
				Thread.sleep(timeout);
				if (hbMsgList.peek() == null) {
					System.out.println("die");
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

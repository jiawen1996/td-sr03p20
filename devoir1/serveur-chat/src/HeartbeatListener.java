import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

public class HeartbeatListener extends Thread {
	private static Queue<Long> hbMsgList = new LinkedList<>();
	private Boolean closed = false;
	private long timeout = 10 * 1000;

	public HeartbeatListener(Queue<Long> hbMsgList, Boolean closed) {
		this.hbMsgList = hbMsgList;
		this.closed = closed;
	}

	public void checkClientAlive() {
		if (hbMsgList.peek() != null) {
			System.out.println("HBListener:\t" + hbMsgList.remove());

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

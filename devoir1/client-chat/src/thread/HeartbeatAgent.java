package thread;

import java.io.IOException;
import java.io.ObjectOutputStream;

import message.HBMessage;

public class HeartbeatAgent extends Thread {
	private int DEFAULT_SAMPLING_PERIOD = 6; // seconds
	private String DEFAULT_NAME = "HeartbeatAgent";
	private Boolean closed = false;
	final ObjectOutputStream oos;

	public HeartbeatAgent(ObjectOutputStream oos, Boolean closed) {
		this.oos = oos;
		this.closed = closed;
	}

	public void sendObject(Object obj) throws IOException {
		this.oos.writeObject(obj);
		this.oos.flush();
	}
	
	public void setClosed(Boolean newClosed) {
		this.closed = newClosed;
	}

	public void run() {
		System.out.println("Running " + DEFAULT_NAME);
		try {
			while (!this.closed) {
				this.sendObject(new HBMessage());
				// Let the thread sleep for a while.
				Thread.sleep(DEFAULT_SAMPLING_PERIOD * 1000);
			}
			this.oos.close();
		} catch (InterruptedException e) {
			System.out.println("Thread " + DEFAULT_NAME + " interrupted.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}

	}

}

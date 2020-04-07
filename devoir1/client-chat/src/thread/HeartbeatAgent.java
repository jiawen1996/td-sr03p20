package thread;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import message.HBMessage;

public class HeartbeatAgent extends Thread {
	private int DEFAULT_SAMPLING_PERIOD = 5; // seconds
	private String DEFAULT_NAME = "HeartbeatAgent";
	long checkDelay = 10;
	long keepAliveDelay = 2000;
	private Boolean closed = false;
	final ObjectOutputStream oos;

	public HeartbeatAgent(ObjectOutputStream oos) {
		this.oos = oos;
	}

	public void sendObject(Object obj) throws IOException {
		this.oos.writeObject(obj);
		this.oos.flush();
	}

	public void run() {
		System.out.println("Running " + DEFAULT_NAME);
		try {
			while (!this.closed) {
//				System.out.println("Thread: " + DEFAULT_NAME + ", " + "I'm alive");
				// Let the thread sleep for a while.
				this.sendObject(new HBMessage());
				Thread.sleep(DEFAULT_SAMPLING_PERIOD * 1000);
			}
		} catch (InterruptedException e) {
			System.out.println("Thread " + DEFAULT_NAME + " interrupted.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Thread " + DEFAULT_NAME + " exiting.");

	}

}

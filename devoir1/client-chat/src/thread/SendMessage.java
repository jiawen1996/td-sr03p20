package thread;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import message.TextMessage;

public class SendMessage extends Thread {
	Scanner scn = new Scanner(System.in);
	final ObjectOutputStream outputStream;
	private Boolean closed = false;

	public SendMessage(ObjectOutputStream outputStream) {
		this.outputStream = outputStream;
	}
	
	public void sendObject(Object obj) throws IOException {
		this.outputStream.writeObject(obj);
		this.outputStream.flush();
	}

	public void run() {
		
		HeartbeatAgent heartbeatAgent = new HeartbeatAgent(outputStream, this.closed);
		heartbeatAgent.setPriority(Thread.MIN_PRIORITY); 
		heartbeatAgent.start();
		
		while (!this.closed) {
			synchronized (this) {
				String sendMsg = scn.nextLine();
				if (sendMsg != null) {
					try {
						this.sendObject(new TextMessage(sendMsg));

						if (sendMsg.equals("exit")) {
							this.closed = true;
							heartbeatAgent.setClosed(true);
							break;
						}
					} catch (IOException ex) {
						// TODO: handle exception
						Logger.getLogger(SendMessage.class.getName()).log(Level.SEVERE, null, ex);
						System.out.println("Échoué à envoyer le message. ");
						break;
					}
				}
			}

		}

		// Fermer le flux
		if (this.closed) {
			try {
				this.outputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}

package thread;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SendMessage extends Thread {
	Scanner scn = new Scanner(System.in);
	final DataOutputStream outputStream;
	private Boolean closed = false;

	public SendMessage(DataOutputStream outputStream) {
		this.outputStream = outputStream;
	}

	public void run() {

		while (!this.closed) {
			synchronized (this) {
				String sendMsg = scn.nextLine();
				if (sendMsg != null) {
					try {
						this.outputStream.write((sendMsg).getBytes());
						this.outputStream.flush();

						if (sendMsg.equals("exit")) {
							this.closed = true;
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

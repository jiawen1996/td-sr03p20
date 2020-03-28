package thread;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * un thread qui intercepte les messages saisis par l’utilisateur et les
 * envoient au serveur à travers la socket de communication.
 *
 * @author jiawen
 * @author linh
 */
public class SendMessage extends Thread {
	Scanner scn = new Scanner(System.in);
	final DataOutputStream outputStream;

	public SendMessage(DataOutputStream outputStream) {
		this.outputStream = outputStream;
	}

	public void run() {

		while (true) {

			String sendMsg = scn.nextLine();
			try {
				outputStream.writeUTF(sendMsg);
			} catch (IOException ex) {
				Logger.getLogger(SendMessage.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
}

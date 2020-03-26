package thread;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

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
				// TODO: handle exception
				Logger.getLogger(SendMessage.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
}

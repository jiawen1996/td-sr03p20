package thread;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReceiveMessage extends Thread {
	final DataInputStream inputStream;

	public ReceiveMessage(DataInputStream inputStream) {
		this.inputStream = inputStream;
	}

	public void run() {

		while (true) {

			try {
				String receiveMsg = inputStream.readUTF();
				System.out.println(receiveMsg);
			} catch (IOException ex) {
				// TODO: handle exception
				Logger.getLogger(SendMessage.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
}

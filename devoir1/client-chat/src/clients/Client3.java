package clients;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.UnknownHostException;

import thread.SendMessage;

import java.net.Socket;
import thread.*;

public class Client3 {
	private final static int ServerPort = 1234;
	private static boolean closed = false; // Signal si le client a quitté

	public static void main(String args[]) throws UnknownHostException, IOException {

		// Établir la connexion
		Socket s = new Socket("localhost", ServerPort);
		System.out.println("Connected : " + s);

		// Récupérer input and out streams
		ObjectOutputStream outputStream = new ObjectOutputStream(s.getOutputStream());
		ObjectInputStream inputStream = new ObjectInputStream(s.getInputStream());
		// Créer des thread pour traiter des messages
		SendMessage sendMessage = new SendMessage(outputStream);
		ReceiveMessage receiveMessage = new ReceiveMessage(s, inputStream);
		// Lacer les thread
		sendMessage.start();
		receiveMessage.start();

	}
}

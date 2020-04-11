/**
 * Contenir la classe Client
 */
package clientPackage;

import java.io.IOException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.UnknownHostException;

import clientThreadPackage.*;

import java.net.Socket;

/**
 * 
 * Établir le socket de connexion entre le client et le serveur. Récupérer les
 * entrées et ainsi les sorties de streams. Créer deux threads pour envoyer et
 * recevoir de message et puis les lance.
 * 
 * @author Linh Nguyen - Jiawen Lyu
 *
 */
public class Client {

	// Le numéro du port de serveur
	private final static int ServerPort = 1234;

	public static void main(String args[]) throws UnknownHostException, IOException {

		// Établir la connexion
		Socket s = new Socket("localhost", ServerPort);
		System.out.println("Connected : " + s);

		// Récupérer input and out streams
		DataInputStream inputStream = new DataInputStream(s.getInputStream());
		DataOutputStream outputStream = new DataOutputStream(s.getOutputStream());

		// Créer des thread pour traiter des messages
		SendMessage sendMessage = new SendMessage(outputStream);
		ReceiveMessage receiveMessage = new ReceiveMessage(s, inputStream);

		// Lancer les thread
		sendMessage.start();
		receiveMessage.start();

	}
}

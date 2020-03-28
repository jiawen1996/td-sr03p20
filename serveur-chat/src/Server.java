import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.PublicKey;
import java.util.Vector;

/**
 * Notre classe de serveur
 *
 * @author jiawen
 * @author linh
 */
public class Server {
	// Vecteur pour stocker les clients actifs
	static Vector<MessageReceptor> listClient = new Vector<>();

	// counter for clients
	static int i = 0;

	public static void main(String[] args) throws IOException {
		ServerSocket serverSocket = new ServerSocket(1234);
		Socket client;

		// Une boucle infinie pour obtenir la demande du client
		while (true) {
			// Accepter la demande entrante
			client = serverSocket.accept();

			System.out.println("New client request received : " + client);

			DataInputStream inputStream = new DataInputStream(client.getInputStream());
			DataOutputStream outputStream = new DataOutputStream(client.getOutputStream());

			System.out.println("Creating a new handler for this client...");

			// Créez un nouvel objet MessageReceptor et un thread pour gérer cette demande
			MessageReceptor newMessageReceptor = new MessageReceptor(client, inputStream, outputStream);
			Thread newClienThread = new Thread(newMessageReceptor);
			System.out.println("Adding this client to active client list");
			listClient.add(newMessageReceptor);

			newClienThread.start();

			// increment i for new client.
			// i is used for naming only, and can be replaced
			// by any naming scheme
			i++;

		}
	}
}

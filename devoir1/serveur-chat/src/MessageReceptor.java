import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import message.HBMessage;
import message.HBResponse;
import message.TextMessage;

public class MessageReceptor extends Thread {

	private static Hashtable<MessageReceptor, String> listClient = new Hashtable<MessageReceptor, String>();
	private Socket client;
	private String clientName;
	private ObjectInputStream inputStream = null;
	private ObjectOutputStream outputStream = null;

	public MessageReceptor(Socket client, Hashtable<MessageReceptor, String> listClient) {
		this.client = client;
		this.listClient = listClient;
		this.clientName = "";
	}

	public void receiveObject() throws IOException, ClassNotFoundException, InterruptedException {
		InputStream in = this.client.getInputStream();
		if (in.available() > 0) {
			System.out.println(in.available());
			Object obj = inputStream.readObject();
			System.out.println("server receive：\t" + obj);
			if (obj instanceof HBMessage) {
				sendObject(new HBResponse());

			} else {
				TextMessage receivedObj = (TextMessage) obj;
				//msg = receivedObj.getMsg();
				
			}
		}
	}

	public void sendObject(Object obj) throws IOException {
		this.outputStream.writeObject(obj);
		System.out.println("server send：\t" + obj);
		this.outputStream.flush();
	}

	@Override
	public void run() {

		try {

			// Récupérer input and out streams de ce socket
			inputStream = new ObjectInputStream(client.getInputStream());
			outputStream = new ObjectOutputStream(client.getOutputStream());

			// Récupérer le pseudonyme d'un client
			String clientName;

			// Assurer qu'il n'y a qu'un seul thread qui utilise cet objet
			synchronized (this) {
				// Demander le pseudonyme
				this.sendObject(new TextMessage("Entrer votre pseudonyme :"));
				InputStream in = this.client.getInputStream();
				boolean isClientNameInitialized = false;
				while (!isClientNameInitialized) {
					if (in.available() > 0) {
						System.out.println(in.available());
						Object obj = inputStream.readObject();
						System.out.println("server receive：\t" + obj);
						if (obj instanceof HBMessage) {
							sendObject(new HBResponse());

						} else {

							TextMessage receivedObj = (TextMessage) obj;
							clientName = receivedObj.getMsg();
							System.out.println("TextMessage：\t" + clientName);
							if ((clientName.indexOf('@') == -1) || (clientName.indexOf('!') == -1)
									|| this.listClient.containsValue(clientName)) {

								// Si pseudonyme unique, il va remplacer la valeur dans listClient
								if (this.listClient.containsValue(clientName)) {
									this.sendObject(new TextMessage("Votre pseudo a été utilisé. Nouveau pseudonyme : "));
									continue;
								} else {
									this.listClient.put(this, clientName);
									this.clientName = clientName;
									isClientNameInitialized = true;
								}
							} else {
								this.outputStream.write(("Le pseudo ne devrait pas contenir '@' ou '!'.").getBytes());
								this.outputStream.flush();
							}
						}
					} else {
						Thread.sleep(10);
					}

				}
			}

			System.out.println("Pseudo de nouveau client : " + this.clientName);
			this.sendObject(new TextMessage(
					this.clientName + " a rejoint la conversation. Tapez 'exit' pour se déconnecter \n"));
			this.sendObject(new TextMessage("------------------------------------------------------------------------------"));

			// Annoncer aux autres clients
			synchronized (this) {
				for (MessageReceptor client : listClient.keySet()) {
					if (client != null && client != this) {
						this.sendObject(new TextMessage(this.clientName + " a  rejoint la conversation"));
					}
				}
			}

			// Commencer la conversation
			// Quit la conversation lorsque le serveur reçoit un message "exit"
			while (true) {
				String msg = "";
				InputStream in = this.client.getInputStream();
				boolean didReceiveMsg = false;
				while(!didReceiveMsg) {
					Object obj = inputStream.readObject();
					System.out.println("server receive：\t" + obj);
					if (obj instanceof HBMessage) {
						sendObject(new HBResponse());

					} else {
						TextMessage receivedObj = (TextMessage) obj;
						msg = receivedObj.getMsg();
						didReceiveMsg = true;
						break;
					}
				}

				// Quitter la conversation
				if (msg.startsWith("exit")) {
					break;
				} else {

					// Diffuser le message aux autres clients
					broadcast(msg, this.clientName);
				}
			}

			// Terminer la session
			this.sendObject(new TextMessage("Vous avez quitté la conversation"));
			System.out.println(this.clientName + " se déconnecte.");
			listClient.remove(this);

			// Annoncer la déconnexion aux autres clients
			synchronized (this) {
				if (!listClient.isEmpty()) {
					for (MessageReceptor client : listClient.keySet()) {

						if (client != null && client != this && client.clientName != null) {
							this.sendObject(new TextMessage("*** " + this.clientName + " a quitté la conversation ***"));
						}
					}
				}
			}

			// Fermer la connexion

			this.inputStream.close();
			this.outputStream.close();
			this.client.close();

		} catch (IOException e) {
			System.out.println("La session de " + this.clientName + " a terminé.");

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Diffuser le message aux autres clients
	 * 
	 * @param msg
	 * @param clientName
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private void broadcast(String msg, String clientName) throws IOException, ClassNotFoundException {
		synchronized (this) {
			for (MessageReceptor client : listClient.keySet()) {

				if (client != null && client.clientName != null && client.clientName != this.clientName) {
					this.sendObject(new TextMessage(clientName + " a dit : " + msg));
				}
			}

			System.out.println("Broadcast message a été envoyé par " + this.clientName);
		}
	}
}

/**
 * Contenir des threads utilisé par la classe Client
 */
package clientThreadPackage;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Un Thread qui traite des messages reçus de client
 * 
 * @author Linh Nguyen - Jiawen Lyu
 *
 */
public class ReceiveMessage extends Thread {
	
	//Le stream d'entré
	private final DataInputStream inputStream;
	
	//Le socket communication de client
	private Socket client;
	
	//Le contenu de message reçu
	private String receivedMsg;
	
	//Le signal permettant savoir si le socket est fermé
	private Boolean closed = false;
	
	public ReceiveMessage(Socket client, DataInputStream inputStream) {
		this.inputStream = inputStream;
		this.client = client;
	}

	
	/**
	 * La méthode de traitement des messages reçus
	 */
	public void run() {

		while (!this.closed) {
			
			try {
				byte b[] = new byte[1024];
				this.inputStream.read(b);
				this.receivedMsg = new String(b);
				
				//Recevoir le message entrant
				if ( this.receivedMsg != null ) {	
					
					synchronized (this) {	
				
						// Quitter la boucle si le socket a fermé, sinon afficher le message sur la console
						if (this.client.isClosed()) {
							this.closed = true;
							break;
						} else {
							System.out.println(this.receivedMsg);
							this.receivedMsg = null;
						}
					}
				}
				
			} catch (IOException ex) {
				Logger.getLogger(SendMessage.class.getName()).log(Level.SEVERE, null, ex);
				System.out.println(" Message reçu est erroné ");
				break;
			}
		}
			
		
		// Attendre que le serveur ferme la connexion
		try {
			this.sleep(20);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		
		// Fermer la connexion
		if (this.closed) {
			try {
				this.inputStream.close();
				this.client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}	
	}
}

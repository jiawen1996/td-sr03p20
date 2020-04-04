/**
 * Contenir des threads utilisé par la classe Client
 */
package clientThreadPackage;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Un Thread pour envoyer le message de client
 * 
 * @author Linh Nguyen - Jiawen Lyu
 */
public class SendMessage extends Thread {
	
	// La variable pour récupérer l'entre sur la console
	private Scanner scanner = new Scanner(System.in);
	
	// Le stream de sortie
	private final DataOutputStream outputStream;
	
	// Le signal permettant savoir si le socket est fermé
	private Boolean closed = false;

	public SendMessage(DataOutputStream outputStream) {
		this.outputStream = outputStream;
	}

	
	/**
	 * Préparer le message et l'envoyer au serveur
	 */
	public void run() {

		while (!this.closed) {
			
			synchronized (this) {
				
				// Récupérer le contenu du message 
				String sendMsg = scanner.nextLine();
				
				if ( sendMsg != null) {
				
					try {
						this.outputStream.write((sendMsg).getBytes());
						this.outputStream.flush();
						
						// Quitter la boucle après avoir envoyer le message de fermeture du socket
						if (sendMsg.equals("exit")) {
							this.closed = true;
							break;
						}
						
					} catch (IOException ex) {
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
				e.printStackTrace();
			}
		}	
	}
}


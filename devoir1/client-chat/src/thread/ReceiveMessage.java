package thread;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.channels.ClosedByInterruptException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReceiveMessage extends Thread {
	private final DataInputStream inputStream;
	private Socket client;
	private String receivedMsg;
	private Boolean closed = false;
	
	
	public ReceiveMessage(Socket client, DataInputStream inputStream) {
		this.inputStream = inputStream;
		this.client = client;
	}

	public void run() {

		// Récupérer les messages jusqu'à quand il reçoit le message de fin
		while (!this.closed) {
			
			try {
				byte b[] = new byte[1024];
				this.inputStream.read(b);
				this.receivedMsg = new String(b);
				
				//Recevoir le message entrant
				if ( this.receivedMsg != null ) {	

					synchronized (this) {	
				
						// Quitter la boucle
						if (this.client.isClosed()) {
							this.closed = true;
							break;
						} else {
							System.out.println(" here ");
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
			// TODO Auto-generated catch block
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

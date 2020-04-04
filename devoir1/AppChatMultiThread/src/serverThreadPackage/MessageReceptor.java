/**
 * Contenir le thread utilisé par la classe Server
 */
package serverThreadPackage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Hashtable;

/**
 * Un Thread qui traite des messages provenance d'un client de serveur
 * 
 * @author Linh Nguyen - Jiawen Lyu
 *
 */
public class MessageReceptor extends Thread{

	// La liste de clients actifs
	private static Hashtable<MessageReceptor, String> listClient = new Hashtable<MessageReceptor, String>();
	
	// Le Socket de communication d'un client
	private Socket client;
	
	// Le pseudonyme du client
	private String clientName;
	
	// Le stream d'entrée
	private DataInputStream inputStream = null;
	
	// Le stream de sortie
	private DataOutputStream outputStream = null;
	
	public MessageReceptor(Socket client, Hashtable<MessageReceptor, String> listClient) {
		this.client = client;
		this.listClient = listClient;
		this.clientName = "";
	}
	
	/**
	 * Communiquer entre le serveur et un client 
	 * Demander le pseudonyme du client
	 */
	@Override
	public void run() {
		
		try {
			
			// Récupérer input and out streams de ce socket 
            inputStream = new DataInputStream(client.getInputStream()); 
            outputStream = new DataOutputStream(client.getOutputStream());
            
            // Le pseudonyme entrant
            String clientName;
            
    		synchronized(this) 
    		{
   				// Demander le pseudonyme
    			this.outputStream.write(("Entrer votre pseudonyme :").getBytes());
    			
    			while (true) {
    				byte b[] = new byte[20];
    				this.inputStream.read(b);
    				clientName = new String(b);

    				// Valider le pseudonyme du client
    				if ((clientName.indexOf('@') == -1) || (clientName.indexOf('!') == -1) || this.listClient.containsValue(clientName)) {
    					
    					if (this.listClient.containsValue(clientName)) {
    						this.outputStream.write(("Votre pseudo a été utilisé. Nouveau pseudonyme : ").getBytes());
    						this.outputStream.flush(); 
    						continue;
    					} else {
    						this.listClient.put(this, clientName);
    	   					this.clientName = clientName;
    	   					break;
    					}
    					
    				} else {
    					this.outputStream.write(("Le pseudo ne devrait pas contenir '@' ou '!'.").getBytes());
    					this.outputStream.flush();
    				}	
    			}
    		}

    		
    		// Afficher le pseudonyme validé
            System.out.println("Pseudo de nouveau client : " + this.clientName); 
            this.outputStream.write((this.clientName + " a rejoint la conversation. Tapez 'exit' pour se déconnecter \n-------------------------------------------------------\n").getBytes());
            this.outputStream.flush();
            
            
            // Annoncer aux autres clients
            synchronized(this)
            {
            	for (MessageReceptor client : listClient.keySet()) {
      				if ( client != null && client != this) {
       					client.outputStream.write((this.clientName + " a  rejoint la conversation").getBytes());
       					this.outputStream.flush();
       				}
       			}
            }
           
           
            /*
             *  Commencer la conversation
             *  Quit la conversation lorsque le serveur reçoit un message "exit"
             */
            while (true) {
            	
            	// Récupérer le message
				byte b[] = new byte[1024];
				this.inputStream.read(b);
				String msg = new String(b);
            	
				// Quitter la conversation lorsqu'il reçoit le message "exit"
				if (msg.startsWith("exit")) {
					break;
				} else {
					
					// Diffuser le message aux autres clients
					broadcast(msg, this.clientName);
				}
			}
      
            
            // Terminer la session
    		this.outputStream.write(("Vous avez quitté la conversation").getBytes());
    		this.outputStream.flush();
    		System.out.println( this.clientName + " se déconnecte.");
    		listClient.remove(this);
    
    		
    		// Annoncer la déconnexion aux autres clients
    		synchronized(this) 
    		{
    			if (!listClient.isEmpty()) {
    				for (MessageReceptor client : listClient.keySet()) {
    
    					if (client != null && client != this && client.clientName != null) {
    						client.outputStream.write(("*** " + this.clientName + " a quitté la conversation ***").getBytes());
    						this.outputStream.flush();
    					}
    				}
    			}
    		}
    
    
    		//Fermer la connexion
    		this.inputStream.close();
    		this.outputStream.close();
    		this.client.close();
    
    	} catch (IOException e) {
    		System.out.println("La session de " + this.clientName + " a terminé.");
    		
    		
    	} catch (ClassNotFoundException e) {
			e.printStackTrace();
    	}
    }

	
	/**
	 * Diffuser le message aux autres clients
	 * @param msg
	 * @param clientName
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private void broadcast(String msg, String clientName) throws IOException, ClassNotFoundException {
		synchronized(this){
			for (MessageReceptor client : listClient.keySet()) {
				
				if (client != null && client.clientName != null && client.clientName!=this.clientName) {
					client.outputStream.write((clientName + " a dit : " + msg).getBytes());
					client.outputStream.flush();
				}
			}
		
			System.out.println("Broadcast message a été envoyé par " + this.clientName);
		}
	}
}

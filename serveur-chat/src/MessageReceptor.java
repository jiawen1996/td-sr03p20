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


public class MessageReceptor extends Thread{
	
	private static Hashtable<MessageReceptor, String> listClient = new Hashtable<MessageReceptor, String>();
	private Socket client;
	private String clientName;
	private DataInputStream inputStream = null;
	private DataOutputStream outputStream = null;
	
	public MessageReceptor(Socket client, Hashtable<MessageReceptor, String> listClient) {
		this.client = client;
		this.listClient = listClient;
		this.clientName = "";
	}
	
	
	@Override
	public void run() {
		
		try {
			
			// Récupérer input and out streams de ce socket 
            inputStream = new DataInputStream(client.getInputStream()); 
            outputStream = new DataOutputStream(client.getOutputStream());
			
            
            // Récupérer le pseudonyme d'un client
            String clientName;
            
            	
            // Assurer qu'il n'y a qu'un seul thread qui utilise cet objet
    		synchronized(this) 
    		{
   				// Demander le pseudonyme
    			this.outputStream.write(("Entrer votre pseudonyme :").getBytes());
    			
    			while (true) {
    				byte b[] = new byte[20];
    				this.inputStream.read(b);
    				clientName = new String(b);

    				if ((clientName.indexOf('@') == -1) || (clientName.indexOf('!') == -1) || this.listClient.containsValue(clientName)) {
    					
    					// Si pseudonyme unique, il va remplacer la valeur dans listClient
    					if (this.listClient.containsValue(clientName)) {
    						this.outputStream.write(("Votre pseudo a été utilisé. Nouveau pseudonyme : ").getBytes());
    						this.outputStream.flush(); // sauvegarder le flux de messages
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
            
            System.out.println("Pseudo de nouveau client : " + this.clientName); 
            this.outputStream.write((this.clientName + " a rejoint la conversation. Tapez 'exit' pour se déconnecter \n").getBytes());
            this.outputStream.write(("------------------------------------------------------------------------------").getBytes());
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
           
           
            // Commencer la conversation
            // Quit la conversation lorsque le serveur reçoit un message "exit"
            while (true) {
            	
            	// Récupérer le message
				byte b[] = new byte[1024];
				this.inputStream.read(b);
				String msg = new String(b);
            	
				// Quitter la conversation
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
			// TODO Auto-generated catch block
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

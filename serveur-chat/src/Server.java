import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class Server {
	// Vector to store active clients 
    static Vector<MessageReceptor> listClient = new Vector<>(); 
      
    // counter for clients 
    static int i = 0; 
  
    public static void main(String[] args) throws IOException  
    { 
        // server is listening on port 1234 
        ServerSocket serverSocket = new ServerSocket(1234); 
          
        Socket client; 
          
        // running infinite loop for getting 
        // client request 
        while (true)  
        { 
            // Accept the incoming request 
            client = serverSocket.accept(); 
  
            System.out.println("New client request received : " + client); 
              
            // obtain input and output streams 
            DataInputStream inputStream = new DataInputStream(client.getInputStream()); 
            DataOutputStream outputStream = new DataOutputStream(client.getOutputStream()); 
              
            System.out.println("Creating a new handler for this client..."); 
  
            // Create a new MessageReceptor object for handling this request. 
            MessageReceptor newMessageReceptor = new MessageReceptor(client,inputStream, outputStream); 
  
            // Create a new Thread with this object. 
            Thread newClienThread = new Thread(newMessageReceptor); 
              
            System.out.println("Adding this client to active client list"); 
  
            // add this client to active clients list 
            listClient.add(newMessageReceptor); 
  
            // start the thread. 
            newClienThread.start(); 
  
            // increment i for new client. 
            // i is used for naming only, and can be replaced 
            // by any naming scheme 
            i++; 
  
        } 
    } 
}

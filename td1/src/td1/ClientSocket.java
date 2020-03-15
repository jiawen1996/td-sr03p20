package td1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientSocket {
	public static void main (String[] args) {
		try {
		Socket client = new Socket ("localhost", 10090); 
		System.out.println("connect réussie");

		DataOutputStream out =  new DataOutputStream(client.getOutputStream());
		DataInputStream ins= new DataInputStream(client.getInputStream());
		
		Scanner sc=new Scanner(System.in);
		String chaine = "";
		
		do {
			String question = ins.readUTF();
			if(question.contains("Prise"))
			{
				System.out.println("test1");
				System.out.println(question);
				
			}else
			{
				System.out.println("test2");
				System.out.println(question);
				chaine=sc.next();
				System.out.println(chaine);
				while (Integer.parseInt(chaine) == 0) {
					System.out.println("Vous ne pouvez pas saissir 0!");
					chaine=sc.next();
				}
				out.writeUTF(chaine);
			}
		} while (!chaine.equals("q"));
		
	
		System.out.println("Fin");
		ins.close();
		out.close();
		client.close();
		
		}catch (IOException ex) {
			Logger.getLogger(ClientSocket.class.getName()).log(Level.SEVERE, null, ex);
		}
		}

}

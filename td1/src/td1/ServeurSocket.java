package td1;

import static java.lang.Math.floor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class ServeurSocket {
	public static int jeu_ordi (int nb_allum, int prise_max)
	{
		int prise = 0;
		int s = 0;
		float t = 0;
		s = prise_max + 1;
		t = ((float) (nb_allum - s)) / (prise_max + 1);
		while (t != floor(t))
		{
			s--;
			t = ((float) (nb_allum-s)) / (prise_max + 1);
		}
		prise = s - 1;
		if (prise == 0)
		prise = 1;
		return (prise);
	}

	public static String afficher_allu(int n)
	{
	int i;
	String c = "\n";
	
	// System.out.print("\n");
	 for (i=0; i<n ;i++)
		 c+="  o";
	 c+="\n";
	  for (i=0; i<n; i++)
		  c+="  |";
	 // System.out.print("  |");
	  c+="\n";
	// System.out.print("\n"); 
	  for (i=0; i<n; i++)
		  c+="  |";
	  c+="\n";
	return c; 

	}
	
	public static void main (String[] args) {
		int nb_max_d=0; /*nbre d'allumettes maxi au départ*/
		int nb_allu_max=0; /*nbre d'allumettes maxi que l'on peut tirer au maxi*/
		int qui=0; /*qui joue? 0=Nous --- 1=PC*/
		int prise=0; /*nbre d'allumettes prises par le joueur*/
		int nb_allu_rest=0; /*nbre d'allumettes restantes*/
		
		try {
			
			ServerSocket conn = new ServerSocket(10090);
			Socket comm = conn.accept();
			DataOutputStream out =  new DataOutputStream(comm.getOutputStream());
			DataInputStream input= new DataInputStream(comm.getInputStream());
			
			do{
				out.writeUTF("Nombre d'allumettes disposées entre les deux joueurs (entre 10 et 60) :");
				nb_max_d=Integer.parseInt(input.readUTF());
			} while((nb_max_d<10) || (nb_max_d>60));
			do {
				out.writeUTF("\nNombre maximal d'allumettes que l'on peut retirer : ");
				nb_allu_max=Integer.parseInt(input.readUTF());
				if (nb_allu_max >= nb_max_d)
					out.writeUTF("Erreur !");
			}while ((nb_allu_max >= nb_max_d)||(nb_allu_max == 0));
			do{
				out.writeUTF("\nQuel joueur commence? 0--> Joueur ; 1--> Ordinateur : ");
				qui=Integer.parseInt(input.readUTF());
			
			}while ((qui != 0) && (qui != 1));
			
		
			nb_allu_rest = nb_max_d;
			do{
				String s="\nNombre d'allumettes restantes :"+nb_allu_rest;
                s=s+ afficher_allu(nb_allu_rest);
               // out.writeUTF(s);
			//如果是玩家玩，询问要拿起来的火柴数量
			if (qui==0){
				do{
					out.writeUTF(s+"\nCombien d'allumettes souhaitez-vous piocher ? ");
					prise=Integer.parseInt(input.readUTF());
					//if ((prise > nb_allu_rest) || (prise > nb_allu_max)){
					//	out.writeUTF("Erreur !\n");
					//}
				}while ((prise > nb_allu_rest) || (prise > nb_allu_max));
				nb_allu_rest = nb_allu_rest - prise;
			
			}else{
				prise = jeu_ordi (nb_allu_rest , nb_allu_max);
				nb_allu_rest = nb_allu_rest - prise;
				//out.writeUTF(s+"\nPrise de l'ordi :"+prise+afficher_allu(nb_allu_rest));
				out.writeUTF(s+"\nPrise de l'ordi :"+prise);
				}
			qui=(qui+1)%2;
			}while (nb_allu_rest >0);
			
			
			if (qui == 0) /* Cest à nous de jouer */
				out.writeUTF("\nVous avez gagné!\n");
			else
				out.writeUTF("\nVous avez perdu!\n");
			input.close();
			out.close();
			conn.close();
			
			
			

		} catch (IOException ex) {
			ex.printStackTrace();
		
			//Logger.getLogger(ServerSocket.class.getName()).log(Level.SEVERE, null, ex);
		}
		
	}
	
	

}

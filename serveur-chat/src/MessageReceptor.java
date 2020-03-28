import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.security.PublicKey;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * une classe d'assistance pour gérer diverses demandes.
 *
 * @author jiawen
 * @author linh
 */
public class MessageReceptor extends Thread {
	private Socket client;
	final DataInputStream inputStream;
	final DataOutputStream outputStream;

	public MessageReceptor(Socket client, DataInputStream inputStream, DataOutputStream outputStream) {
		this.client = client;
		this.inputStream = inputStream;
		this.outputStream = outputStream;
	}

	@Override
	public void run() {
		String receivedMsg = "";
		try {
			receivedMsg = inputStream.readUTF();
			System.out.println(receivedMsg);
			this.client.close();
			this.inputStream.close();
			this.outputStream.close();
		} catch (IOException ex) {
			// TODO: handle exception
			Logger.getLogger(MessageReceptor.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}

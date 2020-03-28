import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.UnknownHostException;
import java.util.Scanner;

import thread.SendMessage;

import java.net.Socket;
import thread.*;

public class Client {
	final static int ServerPort = 1234;

	public static void main(String args[]) throws UnknownHostException, IOException {
		Scanner scn = new Scanner(System.in);

		// getting localhost ip
//        InetAddress ip = InetAddress.getByName("localhost"); 

		// establish the connection
		Socket s = new Socket("localhost", ServerPort);

		DataInputStream inputStream = new DataInputStream(s.getInputStream());
		DataOutputStream outputStream = new DataOutputStream(s.getOutputStream());

		SendMessage sendMessage = new SendMessage(outputStream);
		ReceiveMessage receiveMessage = new ReceiveMessage(inputStream);
		Thread sendMessageThread = new Thread(sendMessage);
		Thread receiveMessageThread = new Thread(receiveMessage);
		sendMessageThread.start();
		receiveMessageThread.start();

	}
}

import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.UnknownHostException;
import java.util.Scanner;

import thread.SendMessage;

import java.net.Socket;
import thread.*;

public class Client2 {
	final static int ServerPort = 1234;

	public static void main(String args[]) throws UnknownHostException, IOException {
		Scanner scn = new Scanner(System.in);

		// getting localhost ip
//        InetAddress ip = InetAddress.getByName("localhost"); 

		// establish the connection
		Socket s = new Socket("localhost", ServerPort);

		// obtaining input and out streams
		DataInputStream inputStream = new DataInputStream(s.getInputStream());
		DataOutputStream outputStream = new DataOutputStream(s.getOutputStream());

		// sendMessage thread
//		Thread sendMessage = new Thread(new Runnable() {
//			@Override
//			public void run() {
//				while (true) {
//
//					// read the message to deliver.
//					String sendMsg = scn.nextLine();
//
//					try {
//						// write on the output stream
//						outputStream.writeUTF(sendMsg);
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//				}
//			}
//		});
//
//		// readMessage thread
//		Thread readMessage = new Thread(new Runnable() {
//			@Override
//			public void run() {
//
//				while (true) {
//					try {
//						// read the message sent to this client
//						String receiveMsg = inputStream.readUTF();
//						System.out.println(receiveMsg);
//					} catch (IOException e) {
//
//						e.printStackTrace();
//					}
//				}
//			}
//		});
		SendMessage sendMessage = new SendMessage(outputStream);
		ReceiveMessage receiveMessage = new ReceiveMessage(inputStream);
		Thread sendMessageThread = new Thread(sendMessage);
		Thread receiveMessageThread = new Thread(receiveMessage);
		sendMessageThread.start();
		receiveMessageThread.start();

	}
}

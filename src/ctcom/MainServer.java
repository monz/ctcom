package ctcom;

import java.io.IOException;
import java.net.Socket;

import javax.naming.OperationNotSupportedException;

import ctcom.messageImpl.CtcomMessage;

public class MainServer {
	public static void main(String[] args) {
		System.out.println("Starting server");
		
		int port = 4242;
		
		try {
			CtcomServer s = new CtcomServer(port);
			Socket client;
			while (true) {
				client = s.accept();
				CtcomMessage m = s.getConnectRequest(client);
				s.sendConnectAcknowledge(m);
				while (true) { // replace true: while connection is established (not quitted)
					m = s.readData();
				}
			}
		} catch (OperationNotSupportedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

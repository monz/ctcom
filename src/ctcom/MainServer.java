package ctcom;

import java.net.Socket;

import javax.naming.OperationNotSupportedException;

import ctcom.messageImpl.CtcomMessage;

public class MainServer {
	public static void main(String[] args) {
		System.out.println("Starting server");
		
		CtcomServer s = new CtcomServer();
		
		try {
			Socket client = s.open(4242);
			while (true) {
				CtcomMessage m = s.receiveConnectRequest(client);
				s.sendConnectAcknowledge(m);
				while (true) { // replace true: while connection is established (not quitted)
					m = s.readData();
				}
			}
		} catch (OperationNotSupportedException e) {
			e.printStackTrace();
		}
	}
}

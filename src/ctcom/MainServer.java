package ctcom;

import java.io.IOException;

import javax.naming.OperationNotSupportedException;

import ctcom.messageImpl.CtcomMessage;
import ctcom.messageImpl.QuitMessage;
import ctcom.messageImpl.ReadDataMessage;
import ctcom.messageTypes.MessageType;

public class MainServer {
	public static void main(String[] args) {
		System.out.println("Starting server");
		
		int port = 4242;
		
		try {
			CtcomServer s = new CtcomServer(port);
			
			boolean quit;
			CtcomMessage m;
			
			while (true) {
				s.accept();
				// receive connect message
				m = s.getMessage();
				if ( m != null &&  m.getType() == MessageType.CONNECT ) {
					// send ctcom acknowledge
					s.sendConnectAcknowledge(m);
				} else {
					continue;
				}
				// read data/quit message
				quit = false;
				while ( ! quit ) {
					m = s.getMessage();
					if ( m != null && m.getType() == MessageType.QUIT ) {
						System.out.println(((QuitMessage)m).getMessage());
						s.getClientSocket().close();
						quit = true;
					} else if ( m != null && m.getType() == MessageType.READ_DATA ) {
						ReadDataMessage rdm = ((ReadDataMessage)m);
						System.out.println("Transfer: " + rdm.getTransfer());
						System.out.println("Location: " + rdm.getLocation());
					}
				}
			}
		} catch (OperationNotSupportedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

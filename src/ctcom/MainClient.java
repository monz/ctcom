package ctcom;

import javax.naming.OperationNotSupportedException;

import ctcom.messageImpl.ConnectMessage;
import ctcom.messageImpl.CtcomMessage;
import ctcom.messageImpl.ReadDataMessage;
import ctcom.messageTypes.MessageType;

public class MainClient {
	public static void main(String[] args) {
		System.out.println("Starting client");
		
		CtcomClient c = new CtcomClient("localhost", 4242);
		
		ConnectMessage m = new ConnectMessage();
		m.addToTestbenchRead("header");
		m.addToTestbenchRead("channelInfo");
		
		m.addToTestbenchWrite("partInfo");
		m.addToTestbenchWrite("parts.engineInputs");
		m.addToTestbenchWrite("parts.engineOutputs");
		m.addToTestbenchWrite("windows");

		try {
			c.sendConnectionRequest(m);
			
			CtcomMessage dataMessage;
			boolean quit = false;
			int counter = 0;
			while ( ! quit && counter < 3 ) {
				dataMessage = c.getMessage();
				if ( dataMessage != null && dataMessage.getType() == MessageType.QUIT ) {
					quit = true;
				} else if ( dataMessage != null && dataMessage.getType() == MessageType.READ_DATA ) {
					ReadDataMessage rdm = ((ReadDataMessage)dataMessage);
					System.out.println("Transfer: " + rdm.getTransfer());
					System.out.println("Location: " + rdm.getLocation());
				}
				counter += 1;
			}
			c.quit("this is it, close this shit!");
		} catch (OperationNotSupportedException e) {
			e.printStackTrace();
		}
	}
}

package ctcom;

import java.io.IOException;

import ctcom.exceptions.ReadMessageException;
import ctcom.messageImpl.ConnectMessage;
import ctcom.messageImpl.ReadDataMessage;

public class MainClient {
	public static void main(String[] args) {
		System.out.println("Starting client");
		
		CtcomClient c = new CtcomClient("localhost", 4242);
		
		try {
			
			ConnectMessage message = new ConnectMessage(null);
			
			message.addToTestbenchRead("header");
			message.addToTestbenchRead("channelInfo");
	
			ReadDataMessage message2 = new ReadDataMessage(null);
			message2.setLocation("/my/path/to/a/file");

			c.serveMessage(message2);
			c.serveMessage(message);
			c.shutdownConnection();
			
		} catch (ReadMessageException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

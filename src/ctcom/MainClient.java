package ctcom;

import java.io.IOException;

import ctcom.messageTypes.ConnectMessage;
import ctcom.messageTypes.ReadDataMessage;

public class MainClient {
	public static void main(String[] args) {
		System.out.println("Starting client");
		
		CtcomClient c = new CtcomClient("localhost", 4242);
		ConnectMessage message = new ConnectMessage();
		
		message.addToTestbenchRead("header");
		message.addToTestbenchRead("channelInfo");

		ReadDataMessage message2 = new ReadDataMessage("/my/path/to/a/file");

		try {
			c.serveMessage(message2);
			c.serveMessage(message);
			c.shutdownConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

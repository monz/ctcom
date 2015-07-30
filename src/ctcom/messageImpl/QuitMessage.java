package ctcom.messageImpl;

import java.io.IOException;
import java.net.Socket;

import ctcom.messageTypes.MessageIdentifier;
import ctcom.messageTypes.MessageType;

public class QuitMessage extends CtcomMessage {
	private String message;
	
	public enum Identifier implements MessageIdentifier {
		TYPE, MESSAGE
	}
	
	public QuitMessage(String message) {
		this.type = MessageType.QUIT;
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}

	@Override
	protected void preparePayload() {
		appendPayload(Identifier.TYPE, formatIdentifier(type));
		appendPayload(Identifier.MESSAGE, message);
	}

	@Override
	public void readMessage(Socket client) throws IOException {
		// TODO Auto-generated method stub
		
	}
}

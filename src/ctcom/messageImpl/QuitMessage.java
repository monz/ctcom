package ctcom.messageImpl;

import ctcom.exceptions.ReadMessageException;
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
	protected void processLine(String line) throws ReadMessageException {
		String[] keyValue = line.split("=");

		// trim "-sign of values
		keyValue[1] = keyValue[1].replaceAll("\"", "");

		// check if message type is correct
		if ( keyValue[0].equals(formatIdentifier(Identifier.TYPE)) ) {
			if ( keyValue[1].equals(MessageType.QUIT.toString()) ) { 
				return; // has correct type
			} else {
				throw new ReadMessageException("Expected ctcom connect message, received type: '" + keyValue[1] + "'");
			}
		}
		// extract message value
		else if ( keyValue[0].equals(formatIdentifier(Identifier.MESSAGE)) ) {
			message = keyValue[1].trim();
		}
		// false key, throw exception
		else {
			throw new ReadMessageException("Unknown key in connect message: '" + keyValue[0] + "'");
		}
	}
}

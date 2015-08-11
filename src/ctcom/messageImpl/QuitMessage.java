package ctcom.messageImpl;

import ctcom.exceptions.ReadMessageException;
import ctcom.messageTypes.MessageIdentifier;
import ctcom.messageTypes.MessageType;

public class QuitMessage extends CtcomMessage {
	private String message;
	
	public enum Identifier implements MessageIdentifier {
		TYPE, MESSAGE
	}
	
	public QuitMessage() {
		this.type = MessageType.QUIT;
	}
	
	/**
	 * Create ctcom quit message object. The object's attributes get filled
	 * with the information extracted from the message string.
	 * @param messageString - complete ctcom message, received from ctcom connection
	 * @throws ReadMessageException
	 */
	public QuitMessage(String messageString) throws ReadMessageException {
		super(messageString);
		this.type = MessageType.QUIT;
	}
	
	/**
	 * Set message attribute 
	 * @param message - reason why connection get closed
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
	/**
	 * Return message attribute
	 * @return
	 */
	public String getMessage() {
		return message;
	}

	@Override
	protected void preparePayload() {
		appendPayload(Identifier.TYPE, type.toString());
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
				throw new ReadMessageException("Expected ctcom quit message, received type: '" + keyValue[1] + "'");
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

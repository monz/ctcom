package ctcom.messageImpl;

import ctcom.exceptions.ReadMessageException;
import ctcom.messageTypes.MessageIdentifier;
import ctcom.messageTypes.MessageType;

public class ReadDataMessage extends CtcomMessage {
	private static String TRANSFER = "file";
	private String location;
	
	public enum Identifier implements MessageIdentifier {
		TYPE, TRANSFER, LOCATION
	}
	
	public ReadDataMessage(String messageString) throws ReadMessageException {
		super(messageString);
		this.type = MessageType.READ_DATA;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}
	
	public String getLocation() {
		return location;
	}
	
	public String getTransfer() {
		return TRANSFER;
	}

	@Override
	protected void preparePayload() {
		appendPayload(Identifier.TYPE, formatIdentifier(type));
		appendPayload(Identifier.TRANSFER, TRANSFER);
		appendPayload(Identifier.LOCATION, location);
	}
	
	@Override
	protected void processLine(String line) throws ReadMessageException {
		String[] keyValue = line.split("=");

		// trim "-sign of values
		keyValue[1] = keyValue[1].replaceAll("\"", "");

		// check if message type is correct
		if ( keyValue[0].equals(formatIdentifier(Identifier.TYPE)) ) {
			if ( keyValue[1].equals(MessageType.READ_DATA.toString()) ) { 
				return; // has correct type
			} else {
				throw new ReadMessageException("Expected ctcom readData message, received type: '" + keyValue[1] + "'");
			}
		}
		// extract transfer value
		else if ( keyValue[0].equals(formatIdentifier(Identifier.TRANSFER)) ) {
			// nothing to do here. With protocol '2014.01' there is only the 'file' method available.
			// This is set into a constant member attribute
			
			// transfer = keyValue[1].trim();
		}
		// extract file location value
		else if ( keyValue[0].equals(formatIdentifier(Identifier.LOCATION)) ) {
			location = keyValue[1].trim();
		}
		// false key, throw exception
		else {
			throw new ReadMessageException("Unknown key in connect message: '" + keyValue[0] + "'");
		}

	}
}

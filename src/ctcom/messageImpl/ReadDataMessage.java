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
	
	public ReadDataMessage() {
		this.type = MessageType.READ_DATA;
	}
	
	/**
	 * Create ctcom read data message object. The object's attributes get filled
	 * with the information extracted from the message string.
	 * @param messageString - complete ctcom message, received from ctcom connection
	 * @throws ReadMessageException
	 */
	public ReadDataMessage(String messageString) throws ReadMessageException {
		super(messageString);
		this.type = MessageType.READ_DATA;
	}
	
	/**
	 * Set location attribute
	 * @param location - full ctmat file path, e.g. network share
	 */
	public void setLocation(String location) {
		this.location = location;
	}
	
	/**
	 * Return location attribute
	 * @return
	 */
	public String getLocation() {
		return location;
	}
	
	/**
	 * Return transfer attribute
	 * @return
	 */
	public String getTransfer() {
		return TRANSFER;
	}

	@Override
	protected void preparePayload() {
		appendPayload(Identifier.TYPE, type.toString());
		appendPayload(Identifier.TRANSFER, TRANSFER);
		appendPayload(Identifier.LOCATION, location);
	}
	
	@Override
	protected void processLine(String line) throws ReadMessageException {
		String[] keyValue = normalizeLine(line);

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

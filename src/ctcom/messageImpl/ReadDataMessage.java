package ctcom.messageImpl;

import ctcom.messageTypes.MessageIdentifier;
import ctcom.messageTypes.MessageType;

public class ReadDataMessage extends CtcomMessage {
	private static String TRANSFER = "file";
	private String location;
	
	public enum Identifier implements MessageIdentifier {
		TYPE, TRANSFER, LOCATION
	}
	
	public ReadDataMessage(String location) {
		this.type = MessageType.READ_DATA;
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
}

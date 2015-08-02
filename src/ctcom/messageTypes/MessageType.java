package ctcom.messageTypes;

public enum MessageType implements MessageIdentifier {
	CONNECT, READ_DATA, QUIT;
	
	@Override
	public String toString() {
		return this.name().toLowerCase();
	}
}

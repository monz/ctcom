package ctcom.messageTypes;

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
	public void preparePayload() {
		appendPayload(Identifier.TYPE, formatIdentifier(type));
		appendPayload(Identifier.MESSAGE, message);
	}
}

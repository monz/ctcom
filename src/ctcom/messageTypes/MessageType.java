package ctcom.messageTypes;

public enum MessageType implements MessageIdentifier {
	CONNECT, READ_DATA, QUIT;
	
	@Override
	public String toString() {
		String messageType = this.name();
		String formattedMessageType;
		
		formattedMessageType = messageType.toString().toLowerCase();
		int index = formattedMessageType.indexOf("_");
		while( index != -1) {
			// extract character next to underscore
			Character c = formattedMessageType.charAt(index+1);
			// remove underscore and replace character next to it with upper case character
			formattedMessageType = formattedMessageType.substring(0, index) + c.toString().toUpperCase() + formattedMessageType.substring(index+2);

			index = formattedMessageType.indexOf("_");
		}
		
		return formattedMessageType;
	}
}

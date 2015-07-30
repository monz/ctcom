package ctcom.messageImpl;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ctcom.messageTypes.MessageIdentifier;
import ctcom.messageTypes.MessageType;

public abstract class CtcomMessage {
	protected MessageType type;
	Map<MessageIdentifier, List<String>> payload = new HashMap<MessageIdentifier, List<String>>();
	
	
	public MessageType getType() {
		return type;
	}
	
	/**
	 * Returns a string representation of the message's payload.
	 * Also includes message begin and end symbols.
	 */
	public String getPayload() {
		preparePayload();
		
		StringBuilder payloadAsString = new StringBuilder();
		
		// begin message
		payloadAsString.append("<");
		payloadAsString.append(System.lineSeparator());
				
		for ( MessageIdentifier identifier : payload.keySet() ) {
			// add identifier
			payloadAsString.append(formatIdentifier(identifier));
			payloadAsString.append("=");
			
			// add values comma separated
			payloadAsString.append('"');
			// payloadAsString.append(String.join(",", payload.get(identifier))); // java 1.8 only
			payloadAsString.append(join(",", payload.get(identifier)));
			payloadAsString.append('"');
			
			// separate lines
			payloadAsString.append(System.lineSeparator());
		}

		// end message
		payloadAsString.append(">");
		payloadAsString.append(System.lineSeparator());
		
		return payloadAsString.toString();
	}
	
	/**
	 * Joins Strings in the collection separated by delimiter
	 */
	private String join(CharSequence delemiter, Collection<String> collection) {
		StringBuilder joinedString = new StringBuilder();
		
		int collectionSize = collection.size();
		int count = 0;
		for (String s : collection) {
			count++;
			joinedString.append(s);
			
			if (count != collectionSize) {
				joinedString.append(delemiter);
			}
		}
		return joinedString.toString();
	}
	
	/**
	 * Format String representation of enum identifier,
	 * to fit ctcom protocol case sensitive identifier representation
	 */
	protected String formatIdentifier(MessageIdentifier identifier) {
		String formattedIdentifier;
		
		formattedIdentifier = identifier.toString().toLowerCase();
		int index = formattedIdentifier.indexOf("_");
		while( index != -1) {
			// extract character next to underscore
			Character c = formattedIdentifier.charAt(index+1);
			// remove underscore and replace character next to it with upper case character
			formattedIdentifier = formattedIdentifier.substring(0, index) + c.toString().toUpperCase() + formattedIdentifier.substring(index+2);

			index = formattedIdentifier.indexOf("_");
		}
		
		return formattedIdentifier;
	}
	
	/**
	 * Set part of a message's payload
	 */
	protected void appendPayload(MessageIdentifier id, List<String> values) {
		payload.put(id, values);
	}

	/**
	 * Set part of a message's payload
	 */
	protected void appendPayload(MessageIdentifier id, String value) {
		payload.put(id, Arrays.asList(value));
	}
	
	/**
	 * Fills the payload with message specific data
	 */
	public abstract void preparePayload();
}

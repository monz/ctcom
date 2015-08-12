package ctcom.messageImpl;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ctcom.exceptions.ReadMessageException;
import ctcom.messageTypes.MessageIdentifier;
import ctcom.messageTypes.MessageType;

public abstract class CtcomMessage {
	protected MessageType type;
	private Map<MessageIdentifier, List<String>> payload = new HashMap<MessageIdentifier, List<String>>();
	
	public CtcomMessage() {
		// explicit defining empty constructor
	}
	
	/**
	 * Create ctcom message object. The object's attributes get filled
	 * with the information extracted from the message string.
	 * @param messageString - complete ctcom message, received from ctcom connection
	 * @throws ReadMessageException
	 */
	public CtcomMessage(String messageString) throws ReadMessageException {
		String workingMessage = messageString.trim();
		
		// validate message string
		if ( ! workingMessage.startsWith("<") || ! workingMessage.endsWith(">") ) {
			throw new ReadMessageException("Malformed message string");
		}
		
		for ( String line : workingMessage.split(System.lineSeparator()) ) {
			// process line
			if ( line.contains("=") ) {
				processLine(line);
				continue;
			}
			// skip beginning
			else if ( line.startsWith("<") ) {
				continue;
			}
			// skip empty lines, or lines containing whitespace only
			else if ( line.isEmpty() || line.trim().isEmpty() ) {
				continue;
			}
			// end of message
			else if ( line.startsWith(">") ) {
				continue;
			}
			throw new ReadMessageException("Malformed line in message: '" + line + "'");
		}
	}
	
	/**
	 * Normalize line expects a raw line received from ctcom connection.
	 * To normalize the line it will be split into a key and value pair.
	 * Also unnecessary whitespace at the beginning and at the end of
	 * the line get removed. Furthermore the quotation marks get removed,
	 * which are surrounding the values.
	 * @param line
	 * @return
	 */
	protected String[] normalizeLine(String line) {
		String[] keyValue = line.split("=");

		// trim whitespace of key
		keyValue[0] = keyValue[0].trim();
		// trim "-sign of values
		keyValue[1] = keyValue[1].replaceAll("\"", "");
		// trim whitespace of values
		keyValue[1] = keyValue[1].trim();
		
		return keyValue;
	}
	
	/**
	 * Return ctcom message type
	 * @return
	 */
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
	 * Fills the payload attribute with message specific data. The
	 * payload represents a ctcom message sent over a ctcom connection.
	 */
	protected abstract void preparePayload();
	
	/**
	 * Process single line of ctcom message to fill object attributes
	 * with data extracted from the line.
	 * @param line extracted line of readMessage method
	 * @throws ReadMessageException
	 */
	protected abstract void processLine(String line) throws ReadMessageException;
}

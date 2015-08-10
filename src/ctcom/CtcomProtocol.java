package ctcom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import ctcom.exceptions.ReadMessageException;
import ctcom.messageImpl.QuitMessage.Identifier;
import ctcom.messageTypes.MessageType;

public abstract class CtcomProtocol {
	
	/**
	 * Read data from client input stream
	 * @throws IOException 
	 * @throws ReadMessageException 
	 */
	public String getMessageString(Socket connection) throws IOException, ReadMessageException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		StringBuffer message = new StringBuffer();
		
		boolean messageEnd = false;
		String line = reader.readLine();
		while ( line != null && ! messageEnd ) {

			// process line
			if ( line.startsWith("<") || line.contains("=") ) {
				message.append(line);
				message.append(System.lineSeparator());
				line = reader.readLine();
				continue;
			}
			// skip empty lines, or lines containing whitespace only
			else if ( line.isEmpty() || line.trim().isEmpty() ) {
				line = reader.readLine();
				continue;
			}
			// end of message
			else if ( line.startsWith(">") ) {
				message.append(line);
				message.append(System.lineSeparator());
				messageEnd = true;
				continue;
			}			
						
			throw new ReadMessageException("Malformed line in message: '" + line + "'");
		}
		return message.toString();
	}
	
	/**
	 * Extract message type of a message string
	 * @param messageString
	 * @return
	 */
	public MessageType getMessageType(String messageString) {
		String[] keyValue;
		for ( String line : messageString.split(System.lineSeparator()) ) {
			// process line
			if ( line.contains("=") ) {
				keyValue = line.split("=");
				// trim "-sign of values
				keyValue[1] = keyValue[1].replaceAll("\"", "");
				
				// check if message type is correct
				if ( keyValue[0].equals(Identifier.TYPE.toString().toLowerCase()) ) {
					return MessageType.valueOf(format(keyValue[1]));
				}
			}
			// skip all other line
			else {
				continue;
			}
		}
		return null;
	}
	
	private String format(String identifier) {
		StringBuffer formattedIdentifier = new StringBuffer();
		
		char c;
		for ( int i = 0; i < identifier.length(); i++ ) {
			c = identifier.charAt(i);
			if ( Character.isLowerCase(c) ) {
				formattedIdentifier.append(c);
			} else if ( Character.isUpperCase(c) ) {
				formattedIdentifier.append("_");
				formattedIdentifier.append(c);
			}
		}
		
		return formattedIdentifier.toString().toUpperCase();
	}
}
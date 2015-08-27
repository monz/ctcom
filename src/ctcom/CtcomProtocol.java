package ctcom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import ctcom.exceptions.ReadMessageException;
import ctcom.messageImpl.QuitMessage.Identifier;
import ctcom.messageTypes.MessageType;

public abstract class CtcomProtocol {
	
	private String lastReceivedMessageString;

	/**
	 * Read data from client input stream
	 * @throws IOException 
	 * @throws ReadMessageException 
	 * @throws TimeoutException 
	 */
	public String getMessageString(final Socket connection, int timeout) throws IOException, ReadMessageException, TimeoutException {
		// define anonymous ctcom message reading task, which will later get executed in its own thread
		Callable<String> readCtcomMessageTask = new Callable<String>() {

			@Override
			public String call() throws Exception {
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
				lastReceivedMessageString = message.toString();

				return lastReceivedMessageString;
			}
		};
		
		// initialize executor service for reading ctcom message string from network in an own thread
		// this is done to provide a timeout for the reading process, to avoid endless blocking on message read
		ExecutorService service = Executors.newSingleThreadExecutor();
		
		String message = null;
		try {
			// read ctcom message, timeout can occur
			message = service.submit(readCtcomMessageTask).get(timeout, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		
		service.shutdown();
		
		return message;
	}

	/**
	 * Returns the last successfully received message represented as string
	 * @return
	 */
	public String getLastReceivedMessageString() {
		return lastReceivedMessageString;
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
	
	/**
	 * Ctcom message types are transferred in a different format. Therefore a
	 * reformatting is requered to be able to find the corresponding
	 * ctcom message type enumeration.
	 * @param identifier - message type extracted from a ctcom message
	 * @return properly formatted message type
	 */
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
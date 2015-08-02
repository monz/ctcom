package ctcom.messageImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import ctcom.exceptions.ReadMessageException;
import ctcom.messageTypes.MessageIdentifier;
import ctcom.messageTypes.MessageType;

public class ConnectMessage extends CtcomMessage {
	private static String PROTOCOL_VERSION = "2014.01";
	private List<String> testbenchWrite = new ArrayList<String>();
	private List<String> testbenchRead = new ArrayList<String>();
	
	public enum Identifier implements MessageIdentifier {
		TYPE, PROTOCOL, TESTBENCH_WRITE, TESTBENCH_READ 
	}
	
	public ConnectMessage() {
		this.type = MessageType.CONNECT;
	}
	
	public void addToTestbenchWrite(String value) {
		testbenchWrite.add(value);
	}
	
	public void addToTestbenchRead(String value) {
		testbenchRead.add(value);
	}
	
	public List<String> getTestbenchWrite() {
		return testbenchWrite;
	}
	
	public List<String> getTestbenchRead() {
		return testbenchRead;
	}
	
	@Override
	protected void preparePayload() {
		appendPayload(Identifier.TYPE, formatIdentifier(type));
		appendPayload(Identifier.PROTOCOL, PROTOCOL_VERSION);
		appendPayload(Identifier.TESTBENCH_WRITE, testbenchWrite);
		appendPayload(Identifier.TESTBENCH_READ, testbenchRead);
	}
	
	public String getProtocolVersion() {
		return PROTOCOL_VERSION;
	}

	@Override
	public void readMessage(Socket client) throws IOException, ReadMessageException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
		
		boolean messageEnd = false;
		String line = reader.readLine();
		while ( line != null && ! messageEnd ) {
			
			// process line
			if ( line.contains("=") ) {
				processLine(line);
				line = reader.readLine();
				continue;
			}
			// skip beginning
			else if ( line.startsWith("<") ) {
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
				messageEnd = true;
				continue;
			}

			throw new ReadMessageException("Malformed line in message: '" + line + "'");
		}
	}
	
	private void processLine(String line) throws ReadMessageException {
		String[] keyValue = line.split("=");

		// trim "-sign of values
		keyValue[1] = keyValue[1].replaceAll("\"", "");

		// check if message type is correct
		if ( keyValue[0].equals(formatIdentifier(Identifier.TYPE)) ) {
			if ( keyValue[1].equals(MessageType.CONNECT.toString()) ) { 
				return; // has correct type
			} else {
				throw new ReadMessageException("Expected ctcom connect message, received type: '" + keyValue[1] + "'");
			}
		}
		// check if protocol version matches
		else if ( keyValue[0].equals(formatIdentifier(Identifier.PROTOCOL)) ) {
			if ( keyValue[1].equals(PROTOCOL_VERSION) ) { 
				return; // has correct protocol version
			} else {
				throw new ReadMessageException("Protocol version " + PROTOCOL_VERSION + " expected, but was '" + keyValue[1] + "'");
			}
		}
		// read test bench write data
		else if ( keyValue[0].equals(formatIdentifier((Identifier.TESTBENCH_WRITE))) ){
			String[] values = keyValue[1].split(",");
			for (String v : values) {
				testbenchWrite.add(v.trim());
			}
		}
		// read test bench read data
		else if ( keyValue[0].equals(formatIdentifier(Identifier.TESTBENCH_READ)) ) {
			String[] values = keyValue[1].split(",");
			for (String v : values) {
				testbenchRead.add(v.trim());
			}
		}
		// false key, throw exception
		else {
			throw new ReadMessageException("Unknown key in connect message: '" + keyValue[0] + "'");
		}
	}
}

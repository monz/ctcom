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
		
		String line = reader.readLine();
		while ( line != null ) {
			
			if ( line.contains("=") ) { // process line
				processLine(line);
			} else if ( line.startsWith("<") || line.startsWith(">")) { // skip beginning, ending
				continue;
			} else if ( line.isEmpty() || line.trim().isEmpty() ) { // skip empty lines, or lines containing whitespace only
				continue;
			}
			// next line
			line = reader.readLine();
		}
	}
	
	private void processLine(String line) throws ReadMessageException {
		String[] keyValue = line.split("=");

		// check if message type is correct
		if ( keyValue[0].equals(formatIdentifier(Identifier.TYPE)) && ! keyValue[1].equals(MessageType.CONNECT.toString()) ) {
			throw new ReadMessageException("Expected ctcom connect message, received type: " + keyValue[1]);
		}
		// check if protocol version matches
		else if ( keyValue[0].equals(formatIdentifier(Identifier.PROTOCOL)) && ! keyValue[1].equals(PROTOCOL_VERSION) ) {
			throw new ReadMessageException("Protocol version " + PROTOCOL_VERSION + " expected, but was " + keyValue[1]);
		}
		// read test bench write data
		else if ( keyValue[0].equals(formatIdentifier((Identifier.TESTBENCH_WRITE))) ){
			// TODO split data, fill message object attributes
		}
		// read test bench read data
		else if ( keyValue[0].equals(formatIdentifier(Identifier.TESTBENCH_READ)) ) {
			// TODO split data, fill message object attributes
		}
	}
}

package ctcom.messageImpl;

import java.util.ArrayList;
import java.util.List;

import ctcom.exceptions.ReadMessageException;
import ctcom.messageTypes.MessageIdentifier;
import ctcom.messageTypes.MessageType;

public class ConnectMessage extends CtcomMessage {
	private static String PROTOCOL_VERSION = "2014.01";
	private boolean protocolMatched;
	private List<String> testbenchWrite;
	private List<String> testbenchRead;
	
	public enum Identifier implements MessageIdentifier {
		TYPE, PROTOCOL, TESTBENCH_WRITE, TESTBENCH_READ 
	}
	
	public ConnectMessage() {
		this.type = MessageType.CONNECT;
		// initialize attributes for later usage
		initialize();
	}
	
	/**
	 * Create ctcom connect message object. The object's attributes get filled
	 * with the information extracted from the message string.
	 * @param messageString - complete ctcom message, received from ctcom connection
	 * @throws ReadMessageException
	 */
	public ConnectMessage(String messageString) throws ReadMessageException {
		super(messageString);
		this.type = MessageType.CONNECT;
		// no initialization is required, super constructor calls 
		// processLine methods which is responsible for initialization 
	}
	
	/**
	 * Protocol matched is true if the protocol version in the message string,
	 * which was used to fill the object attributes, is equal to the configured
	 * protocol version.
	 * @return true if version in received message matched configured version at object creation, false otherwise
	 */
	public boolean isProtocolMatched() {
		return protocolMatched;
	}
	
	/**
	 * Add new value to test bench write attribute list
	 * @param value - value to add
	 */
	public void addToTestbenchWrite(String value) {
		testbenchWrite.add(value);
	}
	
	/**
	 * Add new value to test bench read attribute list
	 * @param value - value to add
	 */
	public void addToTestbenchRead(String value) {
		testbenchRead.add(value);
	}
	
	/**
	 * Return all test bench write values
	 * @return list of test bench write entries
	 */
	public List<String> getTestbenchWrite() {
		return testbenchWrite;
	}
	
	/**
	 * Return all test bench read values
	 * @return list of test bench read entries
	 */
	public List<String> getTestbenchRead() {
		return testbenchRead;
	}
	
	@Override
	protected void preparePayload() {
		appendPayload(Identifier.TYPE, type.toString());
		appendPayload(Identifier.PROTOCOL, PROTOCOL_VERSION);
		appendPayload(Identifier.TESTBENCH_WRITE, testbenchWrite);
		appendPayload(Identifier.TESTBENCH_READ, testbenchRead);
	}

	/**
	 * Returns configured protocol version
	 * @return
	 */
	public String getProtocolVersion() {
		return PROTOCOL_VERSION;
	}
	
	@Override
	protected void processLine(String line) throws ReadMessageException {
		// initialize lists premature, because initialization within the
		// constructor is too late. The lists get accessed by the super
		// constructor before the actual constructor has finished
		// initialization
		initialize();
		
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
				protocolMatched = true;
				return;
			} else {
				protocolMatched = false;
				return;
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
	
	/**
	 * Initializes object attributes. If already initialized, nothing happens
	 */
	private void initialize() {
		if ( testbenchWrite == null || testbenchRead == null ) {
			testbenchWrite = new ArrayList<String>();
			testbenchRead = new ArrayList<String>();
		}
	}
}

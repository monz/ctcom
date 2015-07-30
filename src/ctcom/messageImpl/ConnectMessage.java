package ctcom.messageImpl;

import java.util.ArrayList;
import java.util.List;

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
}

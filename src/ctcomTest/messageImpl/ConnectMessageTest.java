package ctcomTest.messageImpl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;

import ctcom.exceptions.ReadMessageException;
import ctcom.messageImpl.ConnectMessage;
import ctcom.messageTypes.MessageType;

public class ConnectMessageTest {
	
	private static final String properMessageString = 
			"< \n"
			+ "type=\"connect\" \n"
			+ "protocol=\"2014.01\" \n"
			+ "testbenchRead=\"header, partInfo\" \n"
			+ "testbenchWrite=\"header,warnings\" \n"
			+ ">";
	
	private static final String carriageReturnMessageString = 
			"< \r\n"
			+ "type=\"connect\" \r\n"
			+ "protocol=\"2014.01\" \r\n"
			+ "testbenchRead=\"header, partInfo\" \r\n"
			+ "testbenchWrite=\"header,warnings\" \r\n"
			+ ">";
	
	private static final String protocolMismatchMessageString = 
			"< \n"
			+ "type=\"connect\" \n"
			+ "protocol=\"2015.42\" \n"							// protocol version mismatch
			+ "testbenchRead=\"header, partInfo\" \n"
			+ "testbenchWrite=\"header,warnings\" \n"
			+ ">";

	private static final String additionalNewLineMessageString = 
			"< \n"
			+ "type=\"connect\" \n"
			+ "\n \n \n"										// additional newlines
			+ "protocol=\"2014.01\" \n"
			+ "testbenchRead=\"header, partInfo\" \n"
			+ "testbenchWrite=\"header,warnings\" \n"
			+ ">";
	
	private static final String additionalWhitespaceMessageString = 
			"< \n"
			+ "type=   \"connect \" \n"
			+ "protocol  =    \" 2014.01 \" \n"
			+ "testbenchRead =\"header  , partInfo\" \n"
			+ "testbenchWrite= \"   header,warnings      \" \n"
			+ ">";

	private static final String missingNewLineMessageString = 
			"< type=\"connect\" \n"								// missing new line after start tag
			+ "protocol=\"2014.01\" \n"
			+ "testbenchRead=\"header, partInfo\" \n"
			+ "testbenchWrite=\"header,warnings\" \n"
			+ ">";
	
	private static final String missingEndTagMessageString = 
			"< \n"
			+ "type=\"connect\" \n"
			+ "protocol=\"2014.01\" \n"
			+ "testbenchRead=\"header, partInfo\" \n"
			+ "testbenchWrite=\"header,warnings\" \n";
	
	private static final String ignoreCaseOnKeysMessageString = 
			"< \n"
			+ "type=\"connect\" \n"
			+ "protocol=\"2014.01\" \n"
			+ "testbenchread=\"header, partInfo\" \n"
			+ "testbenchwrite=\"header,warnings\" \n"
			+ ">";
	
	private static final String malformedKeysMessageString = 
			"< \n"
			+ "type=\"connect\" \n"
			+ "protocol=\"2014.01\" \n"
			+ "testbench_read=\"header, partInfo\" \n"
			+ "testbench_Write=\"header,warnings\" \n"
			+ ">";
	
	private static final String emptyMessageString = "";
	
	private static final String randomMessageString = "gna, blu, foo! # @ <>";
	
	private static final String wrongKeyValueMessageString =
			"< \n"
			+ "foo = \" bar\" \n"
			+ ">";
	
	private static final String expectedTestbenchRead_01 = "header";
	private static final String expectedTestbenchRead_02 = "partInfo";
	private static final String expectedTestbenchWrite_01 = "header";
	private static final String expectedTestbenchWrite_02 = "warnings";
	
	@Test
	public void properMessageStringTest() {
		// test object creation with proper input message string
		ConnectMessage message;
		try {
			message = new ConnectMessage(properMessageString);
			assertNotNull(message);
			
			// check if attributes were filled properly
			validateAttributesProtocolTrue(message);
			
		} catch (ReadMessageException e) {
			e.printStackTrace();
			fail("Could not create ctcom connect message from connect message string");
		}
	}
	
	@Test
	public void carriageReturnMessageStringTest() {
		// test object creation with proper input message string
		// with carriage return line breaks instead of simple newline
		ConnectMessage message;
		try {
			message = new ConnectMessage(carriageReturnMessageString);
			assertNotNull(message);
			
			// check if attributes were filled properly
			validateAttributesProtocolTrue(message);
			
		} catch (ReadMessageException e) {
			e.printStackTrace();
			fail("Could not create ctcom connect message from connect message string");
		}
	}
	
	@Test
	public void protocolVersionMismatchMessageStringTest() {
		// test object creation with proper input message string
		// protocol version is not matching
		ConnectMessage message;
		try {
			message = new ConnectMessage(protocolMismatchMessageString);
			assertNotNull(message);
			
			// check if attributes were filled properly
			validateAttributesProtocolFalse(message);
			
		} catch (ReadMessageException e) {
			e.printStackTrace();
			fail("Could not create ctcom connect message from connect message string");
		}
	}
	
	@Test
	public void additionalNewlinesMessageStringTest() {
		// test object creation with input message string containing
		// additional newlines
		ConnectMessage message;
		try {
			message = new ConnectMessage(additionalNewLineMessageString);
			assertNotNull(message);

			// check if attributes were filled properly			
			validateAttributesProtocolTrue(message);
			
		} catch (ReadMessageException e) {
			e.printStackTrace();
			fail("Could not create ctcom connect message from connect message string");
		}
	}
	
	@Test
	public void additionalWhitespaceMessageStringTest() {
		// test object creation with input message string containing
		// additional whitespace between equal sign and values
		ConnectMessage message;
		try {
			message = new ConnectMessage(additionalWhitespaceMessageString);
			assertNotNull(message);

			// check if attributes were filled properly			
			validateAttributesProtocolTrue(message);
			
		} catch (ReadMessageException e) {
			e.printStackTrace();
			fail("Could not create ctcom connect message from connect message string");
		}
	}
	
	@Test
	public void missingNewLineMessageStringTest() {
		// test object creation with input message string with
		// missing newline after begin tag
		
		try {
			new ConnectMessage(missingNewLineMessageString);
			fail("Should have thrown ReadMessageException");
			
		} catch (ReadMessageException e) {
			// thrown exception was expected
		}
	}
	
	@Test
	public void missingEndTagMessageStringTest() {
		// test object creation with input message string with
		// missing newline after begin tag
		
		try {
			new ConnectMessage(missingEndTagMessageString);
			fail("Should have thrown ReadMessageException");
			
		} catch (ReadMessageException e) {
			// thrown exception was expected
		}
	}
	
	@Test
	public void ignoreCaseOnKeysMessageStringTest() {
		// test object creation with input message string
		// ignoring the case sensitivity of the key names
		
		try {
			new ConnectMessage(ignoreCaseOnKeysMessageString);
			fail("Should have thrown ReadMessageException");
			
		} catch (ReadMessageException e) {
			// thrown exception was expected
		}
	}
	
	@Test
	public void malformedKeysMessageStringTest() {
		// test object creation with input message string
		// containing malformed key names
		
		try {
			new ConnectMessage(malformedKeysMessageString);
			fail("Should have thrown ReadMessageException");
			
		} catch (ReadMessageException e) {
			// thrown exception was expected
		}
	}

	@Test
	public void emptyMessageStringTest() {
		// test object creation with empty input message string
		
		try {
			new ConnectMessage(emptyMessageString);
			fail("Should have thrown ReadMessageException");
			
		} catch (ReadMessageException e) {
			// thrown exception was expected
		}
	}

	@Test
	public void randomMessageStringTest() {
		// test object creation with random input message string
		
		try {
			new ConnectMessage(randomMessageString);
			fail("Should have thrown ReadMessageException");
			
		} catch (ReadMessageException e) {
			// thrown exception was expected
		}
	}
	
	@Test
	public void wrongKeyValueMessageStringTest() {
		// test object creation with input message string
		// containing wrong key value information
		
		try {
			new ConnectMessage(wrongKeyValueMessageString);
			fail("Should have thrown ReadMessageException");
			
		} catch (ReadMessageException e) {
			// thrown exception was expected
		}
	}
	
//	@Test
//	public void getPayloadTest() {
//		// test if message object returns the correct message payload string
//		// NOTE: the order is arbitrary, because the payload is saved within a hash map
//		// TODO modify getPayload method or unit test
//		try {
//			CtcomMessage message = new ConnectMessage(properMessageString);
//			
//			String actualPayload = message.getPayload();
//			String expectedPayload =
//					"<\n"
//					+ "testbenchRead=\"header,partInfo\"\n"
//					+ "protocol=\"2014.01\"\n"
//					+ "type=\"connect\"\n"
//					+ "testbenchWrite=\"header,warnings\"\n"
//					+ ">\n";
//					
//			assertEquals(expectedPayload, actualPayload);
//		} catch (ReadMessageException e) {
//			e.printStackTrace();
//			fail("Could not create ctcom connect message from connect message string");
//		}
//	}
	
	private static void validateAttributes(ConnectMessage message) {
		// check if attributes were filled properly
		assertTrue(message.getType() == MessageType.CONNECT);
		
		List<String> actualTestbenchRead = message.getTestbenchRead();
		List<String> actualTestbenchWrite = message.getTestbenchWrite();
		assertTrue(actualTestbenchRead.size() == 2);
		assertTrue(actualTestbenchWrite.size() == 2);
		
		// test bench read
		assertTrue(actualTestbenchRead.contains(expectedTestbenchRead_01));
		assertTrue(actualTestbenchRead.contains(expectedTestbenchRead_02));
		
		// test bench write
		assertTrue(actualTestbenchWrite.contains(expectedTestbenchWrite_01));
		assertTrue(actualTestbenchWrite.contains(expectedTestbenchWrite_02));
	}
	
	private static void validateAttributesProtocolTrue(ConnectMessage message) {
		validateAttributes(message);

		// protocol
		assertTrue(message.isProtocolMatched());
	}
	
	private static void validateAttributesProtocolFalse(ConnectMessage message) {
		validateAttributes(message);
		
		// protocol
		assertFalse(message.isProtocolMatched());
	}
}

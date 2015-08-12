package ctcomTest.messageImpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import ctcom.exceptions.ReadMessageException;
import ctcom.messageImpl.ReadDataMessage;
import ctcom.messageTypes.MessageType;

public class ReadDataMessageTest {
	
	private static final String properMessageString = 
			"< \n"
			+ "type=\"readData\" \n"
			+ "transfer=\"file\" \n"
			+ "location=\"\\\\someserver\\somedirectory\\ct.ctmat\" \n"
			+ ">";
	
	private static final String carriageReturnMessageString = 
			"< \r\n"
			+ "type=\"readData\" \r\n"
			+ "transfer=\"file\" \r\n"
			+ "location=\"\\\\someserver\\somedirectory\\ct.ctmat\" \r\n"
			+ ">";
	
	private static final String malformedTypeValueMessageString = 
			"< \n"
			+ "type=\"readdata\" \n"							// type value ignores case sensitivity
			+ "transfer=\"file\" \n"
			+ "testbenchRead=\"header, partInfo\" \n"
			+ ">";

	private static final String additionalNewLineMessageString = 
			"< \n"
			+ "type=\"readData\" \n"
			+ "\n \n \n"										// additional newlines
			+ "transfer=\"file\" \n"
			+ "location=\"\\\\someserver\\somedirectory\\ct.ctmat\" \n"
			+ ">";
	
	private static final String additionalWhitespaceMessageString = 
			"< \n"
			+ "type=   \"readData \" \n"
			+ "transfer  =    \" files \" \n"
			+ "location =\"  \\\\someserver\\somedirectory\\ct.ctmat \" \n"
			+ ">";
	
	private static final String locationWithWhitespaceMessageString = 
			"< \n"
			+ "type=\"readData\" \n"
			+ "transfer=\"file\" \n"
			+ "location=\"\\\\someserver\\some dir ectory\\with Whitespace\\ct.ctmat\" \n"
			+ ">";

	private static final String missingNewLineMessageString = 
			"< type=\"readData\" \n"								// missing new line after start tag
			+ "transfer=\"2014.01\" \n"
			+ "location=\"\\\\someserver\\somedirectory\\ct.ctmat\" \n"
			+ ">";
	
	private static final String missingEndTagMessageString = 
			"< \n"
			+ "type=\"readData\" \n"
			+ "transfer=\"file\" \n"
			+ "location=\"\\\\someserver\\somedirectory\\ct.ctmat\" \n";
	
	private static final String ignoreCaseOnKeysMessageString = 
			"< \n"
			+ "type=\"readData\" \n"
			+ "transfer=\"file\" \n"
			+ "Location=\"\\\\someserver\\somedirectory\\ct.ctmat\" \n"
			+ ">";
	
	private static final String malformedKeysMessageString = 
			"< \n"
			+ "type=\"readData\" \n"
			+ "transfer=\"file\" \n"
			+ "loca_tion=\"\\\\someserver\\somedirectory\\ct.ctmat\" \n"
			+ ">";
	
	private static final String emptyMessageString = "";
	
	private static final String randomMessageString = "gna, blu, foo! # @ <>";
	
	private static final String wrongKeyValueMessageString =
			"< \n"
			+ "foo = \" bar\" \n"
			+ ">";
	
	private static final String expectedLocation_01 = "\\\\someserver\\somedirectory\\ct.ctmat";
	private static final String expectedLocation_02 = "\\\\someserver\\some dir ectory\\with Whitespace\\ct.ctmat";
	
	@Test
	public void properMessageStringTest() {
		// test object creation with proper input message string
		ReadDataMessage message;
		try {
			message = new ReadDataMessage(properMessageString);
			assertNotNull(message);
			
			// check if attributes were filled properly
			validatesAttributesWithoutWhitespaceLocation(message);
			
		} catch (ReadMessageException e) {
			e.printStackTrace();
			fail("Could not create ctcom read data message from connect message string");
		}
	}
	
	@Test
	public void carriageReturnMessageStringTest() {
		// test object creation with proper input message string
		// with carriage return line breaks instead of simple newline
		ReadDataMessage message;
		try {
			message = new ReadDataMessage(carriageReturnMessageString);
			assertNotNull(message);
			
			// check if attributes were filled properly
			validatesAttributesWithoutWhitespaceLocation(message);
			
		} catch (ReadMessageException e) {
			e.printStackTrace();
			fail("Could not create ctcom read data message from connect message string");
		}
	}
	
	@Test
	public void locationWithWhitespaceMessageStringTest() {
		// test object creation with proper input message string
		// with a location string containing whitespace
		ReadDataMessage message;
		try {
			message = new ReadDataMessage(locationWithWhitespaceMessageString);
			assertNotNull(message);
			
			// check if attributes were filled properly
			validatesAttributesWithWhitespaceLocation(message);
			
		} catch (ReadMessageException e) {
			e.printStackTrace();
			fail("Could not create ctcom read data message from connect message string");
		}
	}
	
	@Test
	public void protocolVersionMismatchMessageStringTest() {
		// test object creation with input message string
		// containing a malformed type value
		try {
			new ReadDataMessage(malformedTypeValueMessageString);
			fail("Should have thrown ReadMessageException");
			
		} catch (ReadMessageException e) {
			// thrown exception was expected
		}
	}
	
	@Test
	public void additionalNewlinesMessageStringTest() {
		// test object creation with input message string containing
		// additional newlines
		ReadDataMessage message;
		try {
			message = new ReadDataMessage(additionalNewLineMessageString);
			assertNotNull(message);

			// check if attributes were filled properly			
			validatesAttributesWithoutWhitespaceLocation(message);
			
		} catch (ReadMessageException e) {
			e.printStackTrace();
			fail("Could not create ctcom read data message from connect message string");
		}
	}
	
	@Test
	public void additionalWhitespaceMessageStringTest() {
		// test object creation with input message string containing
		// additional whitespace between equal sign and values
		ReadDataMessage message;
		try {
			message = new ReadDataMessage(additionalWhitespaceMessageString);
			assertNotNull(message);

			// check if attributes were filled properly			
			validatesAttributesWithoutWhitespaceLocation(message);
			
		} catch (ReadMessageException e) {
			e.printStackTrace();
			fail("Could not create ctcom read data message from connect message string");
		}
	}
	
	@Test
	public void missingNewLineMessageStringTest() {
		// test object creation with input message string with
		// missing newline after begin tag
		
		try {
			new ReadDataMessage(missingNewLineMessageString);
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
			new ReadDataMessage(missingEndTagMessageString);
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
			new ReadDataMessage(ignoreCaseOnKeysMessageString);
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
			new ReadDataMessage(malformedKeysMessageString);
			fail("Should have thrown ReadMessageException");
			
		} catch (ReadMessageException e) {
			// thrown exception was expected
		}
	}

	@Test
	public void emptyMessageStringTest() {
		// test object creation with empty input message string
		
		try {
			new ReadDataMessage(emptyMessageString);
			fail("Should have thrown ReadMessageException");
			
		} catch (ReadMessageException e) {
			// thrown exception was expected
		}
	}

	@Test
	public void randomMessageStringTest() {
		// test object creation with random input message string
		
		try {
			new ReadDataMessage(randomMessageString);
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
			new ReadDataMessage(wrongKeyValueMessageString);
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
//			CtcomMessage message = new ReadDataMessage(properMessageString);
//			
//			String actualPayload = message.getPayload();
//			String expectedPayload =
//					"<\n"
//					+ "transfer=\"file\"\n"
//					+ "type=\"readData\"\n"
//					+ "location=\"\\\\someserver\\somedirectory\\ct.ctmat\"\n"
//					+ ">\n";
//					
//			assertEquals(expectedPayload, actualPayload);
//		} catch (ReadMessageException e) {
//			e.printStackTrace();
//			fail("Could not create ctcom connect message from connect message string");
//		}
//	}
	
	private static void validatesAttributes(ReadDataMessage message) {
		// check if attributes were filled properly
		assertTrue(message.getType() == MessageType.READ_DATA);
	}
	
	private static void validatesAttributesWithoutWhitespaceLocation(ReadDataMessage message) {
		validatesAttributes(message);
		
		String actualLocation = message.getLocation();
		assertEquals(expectedLocation_01, actualLocation);
	}
	
	private static void validatesAttributesWithWhitespaceLocation(ReadDataMessage message) {
		validatesAttributes(message);
		
		String actualLocation = message.getLocation();
		assertEquals(expectedLocation_02, actualLocation);
	}
}

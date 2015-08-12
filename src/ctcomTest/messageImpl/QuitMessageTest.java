package ctcomTest.messageImpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import ctcom.exceptions.ReadMessageException;
import ctcom.messageImpl.QuitMessage;
import ctcom.messageTypes.MessageType;

public class QuitMessageTest {
	
	private static final String properMessageString = 
			"< \n"
			+ "type=\"quit\" \n"
			+ "message=\"this is the reason why the ctcom connection was closed\" \n"
			+ ">";
	
	private static final String carriageReturnMessageString = 
			"< \r\n"
			+ "type=\"quit\" \r\n"
			+ "message=\"this is the reason why the ctcom connection was closed\" \r\n"
			+ ">";

	private static final String additionalNewLineMessageString = 
			"< \n"
			+ "type=\"quit\" \n"
			+ "\n \n \n"										// additional newlines
			+ "message=\"this is the reason why the ctcom connection was closed\" \n"
			+ ">";
	
	private static final String additionalWhitespaceMessageString = 
			"< \n"
			+ "type=   \"quit \" \n"
			+ "message= \"   this is the reason why the ctcom connection was closed      \" \n"
			+ ">";

	private static final String missingNewLineMessageString = 
			"< type=\"quit\" \n"								// missing new line after start tag
			+ "message=\"this is the reason why the ctcom connection was closed\" \n"
			+ ">";
	
	private static final String missingEndTagMessageString = 
			"< \n"
			+ "type=\"quit\" \n"
			+ "message=\"this is the reason why the ctcom connection was closed\" \n";
	
	private static final String ignoreCaseOnKeysMessageString = 
			"< \n"
			+ "type=\"quit\" \n"
			+ "Message=\"this is the reason why the ctcom connection was closed\" \n"
			+ ">";
	
	private static final String malformedKeysMessageString = 
			"< \n"
			+ "type=\"quit\" \n"
			+ "mesSage=\"this is the reason why the ctcom connection was closed\" \n"
			+ ">";
	
	private static final String emptyMessageString = "";
	
	private static final String randomMessageString = "gna, blu, foo! # @ <>";
	
	private static final String wrongKeyValueMessageString =
			"< \n"
			+ "foo = \" bar\" \n"
			+ ">";
	
	private static final String expectedMessage = "this is the reason why the ctcom connection was closed";
	
	@Test
	public void properMessageStringTest() {
		// test object creation with proper input message string
		QuitMessage message;
		try {
			message = new QuitMessage(properMessageString);
			assertNotNull(message);
			
			// check if attributes were filled properly
			validateAttributes(message);
			
		} catch (ReadMessageException e) {
			e.printStackTrace();
			fail("Could not create ctcom quit message from connect message string");
		}
	}
	
	@Test
	public void carriageReturnMessageStringTest() {
		// test object creation with proper input message string
		// with carriage return line breaks instead of simple newline
		QuitMessage message;
		try {
			message = new QuitMessage(carriageReturnMessageString);
			assertNotNull(message);
			
			// check if attributes were filled properly
			validateAttributes(message);
			
		} catch (ReadMessageException e) {
			e.printStackTrace();
			fail("Could not create ctcom quit message from connect message string");
		}
	}
	
	@Test
	public void additionalNewlinesMessageStringTest() {
		// test object creation with input message string containing
		// additional newlines
		QuitMessage message;
		try {
			message = new QuitMessage(additionalNewLineMessageString);
			assertNotNull(message);

			// check if attributes were filled properly			
			validateAttributes(message);
			
		} catch (ReadMessageException e) {
			e.printStackTrace();
			fail("Could not create ctcom quit message from connect message string");
		}
	}
	
	@Test
	public void additionalWhitespaceMessageStringTest() {
		// test object creation with input message string containing
		// additional whitespace between equal sign and values
		QuitMessage message;
		try {
			message = new QuitMessage(additionalWhitespaceMessageString);
			assertNotNull(message);

			// check if attributes were filled properly			
			validateAttributes(message);
			
		} catch (ReadMessageException e) {
			e.printStackTrace();
			fail("Could not create ctcom quit message from connect message string");
		}
	}
	
	@Test
	public void missingNewLineMessageStringTest() {
		// test object creation with input message string with
		// missing newline after begin tag
		
		try {
			new QuitMessage(missingNewLineMessageString);
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
			new QuitMessage(missingEndTagMessageString);
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
			new QuitMessage(ignoreCaseOnKeysMessageString);
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
			new QuitMessage(malformedKeysMessageString);
			fail("Should have thrown ReadMessageException");
			
		} catch (ReadMessageException e) {
			// thrown exception was expected
		}
	}

	@Test
	public void emptyMessageStringTest() {
		// test object creation with empty input message string
		
		try {
			new QuitMessage(emptyMessageString);
			fail("Should have thrown ReadMessageException");
			
		} catch (ReadMessageException e) {
			// thrown exception was expected
		}
	}

	@Test
	public void randomMessageStringTest() {
		// test object creation with random input message string
		
		try {
			new QuitMessage(randomMessageString);
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
			new QuitMessage(wrongKeyValueMessageString);
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
//			CtcomMessage message = new QuitMessage(properMessageString);
//			
//			String actualPayload = message.getPayload();
//			String expectedPayload =
//					"<\n"
//					+ "message=\"this is the reason why the ctcom connection was closed\"\n"
//					+ "type=\"quit\"\n"
//					+ ">\n";
//					
//			assertEquals(expectedPayload, actualPayload);
//		} catch (ReadMessageException e) {
//			e.printStackTrace();
//			fail("Could not create ctcom connect message from connect message string");
//		}
//	}
	
	private static void validateAttributes(QuitMessage message) {
		// check if attributes were filled properly
		assertTrue(message.getType() == MessageType.QUIT);
		
		String actualQuitMessage = message.getMessage();
		assertEquals(expectedMessage, actualQuitMessage);
	}
}

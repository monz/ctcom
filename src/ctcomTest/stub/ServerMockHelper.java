package ctcomTest.stub;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ServerMockHelper {

	public static final String NO_QUIT = "";
	public static final String QUIT = "<;quit;>";
	public static final String CONNECT_MESSAGE = "<;type;CONNECT;operation;%s;payload;%s;>";
	public static final String READ_DATA_MESSAGE = "<;type;READ_DATA;operation;%s;payload;%s;>";
	public static final String QUIT_MESSAGE = "<;type;QUIT;operation;%s;payload;%s;>";
	
	private static final String WAIT_MESSAGE = "<;type;READ_DATA;operation;WAIT;payload;%d;>"; // type must be valid (can be one of the available types)

	public static final String NO_PAYLOAD = "";

	public enum MessageOperation {
		NONE, SEND, RECEIVE, WAIT
	}
	
	private static void send(Socket s, String formattedMessage) throws IOException {
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
		
		writer.write(formattedMessage);
		writer.newLine();
		writer.flush();
	}
	
	public static void wait(Socket s, int milliseconds) throws IOException {
		String formattedMessage = String.format(WAIT_MESSAGE, milliseconds);
		
		send(s, formattedMessage);
	}

	public static void receiveMessage(Socket s, String message) throws IOException {
		String formattedMessage = String.format(message, MessageOperation.RECEIVE.toString(), NO_PAYLOAD);
		
		send(s, formattedMessage);
	}
	
	public static void sendMessage(Socket s, String message, String messagePayload) throws IOException {		
		// prepare payload
		String preparedPayload = messagePayload.replaceAll("\n", "0x0a");
		
		String formattedMessage = String.format(message, MessageOperation.SEND.toString(), preparedPayload);
		
		send(s, formattedMessage);
	}
}

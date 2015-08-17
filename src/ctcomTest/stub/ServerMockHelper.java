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

	public static final String NO_PAYLOAD = "";

	public enum MessageOperation {
		NONE, SEND, RECEIVE
	}

	public static void ReceiveMessage(Socket s, String message) throws IOException {
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
		String formattedMessage = String.format(message, MessageOperation.RECEIVE.toString(), NO_PAYLOAD);
		
		writer.write(formattedMessage);
		writer.newLine();
		writer.flush();
	}
	
	public static void SendMessage(Socket s, String message, String messagePayload) throws IOException {
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
		
		// prepare payload
		String preparedPayload = messagePayload.replaceAll("\n", "0x0a");
		
		String formattedMessage = String.format(message, MessageOperation.SEND.toString(), preparedPayload);
		
		writer.write(formattedMessage);
		writer.newLine();
		writer.flush();
	}
}

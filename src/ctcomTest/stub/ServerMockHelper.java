package ctcomTest.stub;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class MessageOperation {

	public static final String NO_QUIT = "";
	public static final String QUIT = "<;quit;>";
	public static final String CONNECT_MESSAGE = "<;type;CONNECT;>";
	public static final String READ_DATA_MESSAGE = "<;type;READ_DATA;>";
	public static final String QUIT_MESSAGE = "<;type;QUIT;>";

	public static void SendMessage(Socket s, String message) throws IOException {
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
		
		writer.write(message);
		writer.newLine();
		writer.flush();
	}
}

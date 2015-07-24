package ctcom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class CtcomServer {
	private ServerSocket server;
	private Socket client;
	
	public CtcomServer(int port){
		try {
			this.server = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void accept() throws IOException {
		while (true) {
			client = server.accept();
			serveConnect();
		}
	}
	
	private void serveConnect() throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));

		String line = reader.readLine();
		while ( line != null ) {
			System.out.println(line);
			line = reader.readLine();
		}
	}
}

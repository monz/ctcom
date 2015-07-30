package ctcom;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import ctcom.messageImpl.CtcomMessage;

public class CtcomClient {
	private Socket server;
	
	public CtcomClient(String host, int port) {
		try {
			server = new Socket(host, port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void serve(String message) throws IOException {
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(server.getOutputStream()));
		BufferedReader reader = new BufferedReader(new InputStreamReader(server.getInputStream()));
		
		writer.write(message);
		writer.newLine();
		writer.flush();
		
		String echo = reader.readLine();
		System.out.println(echo);
	}
	
	public void serveMessage(CtcomMessage message) throws IOException {
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(server.getOutputStream()));
		
		writer.write(message.getPayload());
		writer.newLine();
		writer.flush();		
	}
	
	public void shutdownConnection() throws IOException {
		server.close();
	}
}

package ctcom.statesImpl.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import javax.naming.OperationNotSupportedException;

import ctcom.CtcomServer;
import ctcom.messageImpl.CtcomMessage;
import ctcom.messageImpl.QuitMessage;
import ctcom.states.ServerState;

public class EstablishedServerState implements ServerState {

	@Override
	public Socket accept(CtcomServer server) throws OperationNotSupportedException {
		throw new OperationNotSupportedException("Cannot open connection, already open and established.");		
	}

	@Override
	public void close(CtcomServer server) throws OperationNotSupportedException {
		throw new OperationNotSupportedException("Cannot close tcp connection, must quit ctcom first.");
	}

	@Override
	public void sendConnectionAck(CtcomServer server, CtcomMessage message) throws OperationNotSupportedException {
		throw new OperationNotSupportedException("Cannot send connection acknowledge, already established.");
	}

	@Override
	public CtcomMessage readData(CtcomServer server) throws OperationNotSupportedException {
		System.out.println(server.getState()); // debug
		CtcomMessage message = null;
		Socket client = server.getClientSocket();
		
		try {	
			BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
		
			String line = reader.readLine();
			while ( line != null ) {
				System.out.println(line);
				line = reader.readLine();
			}
			System.out.println(server.getState()); // debug
			
		} catch (IOException e) {
			// cannot open stream, or read data
			e.printStackTrace();
		}
		// stay in established server state
		
		// return read data
		return message;
	}

	@Override
	public void quit(CtcomServer server, String message) throws OperationNotSupportedException {
		Socket client = server.getClientSocket();
		try {
			QuitMessage quitMessage = new QuitMessage(message);
			
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
			writer.write(quitMessage.getPayload());
			writer.flush();
		} catch (IOException e) {
			// cannot open stream, or read data
			e.printStackTrace();
		}
		
		server.changeState(new ListenServerState());
	}

	@Override
	public CtcomMessage getConnectRequest(CtcomServer server, Socket client) throws OperationNotSupportedException {
		throw new OperationNotSupportedException("Cannot serve client, connection already established.");
	}

}

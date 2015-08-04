package ctcom.statesImpl.server;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

import javax.naming.OperationNotSupportedException;

import ctcom.CtcomServer;
import ctcom.messageImpl.CtcomMessage;
import ctcom.messageImpl.QuitMessage;
import ctcom.states.ServerState;

public class RequestReceivedServerState implements ServerState {

	@Override
	public Socket accept(CtcomServer server) throws OperationNotSupportedException {
		throw new OperationNotSupportedException("Cannot open connection, already open.");
	}

	@Override
	public void close(CtcomServer server) throws OperationNotSupportedException {
		throw new OperationNotSupportedException("Close TCP connection while half open ctcom connection is not allowed.");
	}

	@Override
	public void sendConnectionAck(CtcomServer server, CtcomMessage message) throws OperationNotSupportedException {
		try {
			Socket client = server.getClientSocket();
			
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
			writer.write(message.getPayload());
			writer.flush();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		server.changeState(new EstablishedServerState());
	}

	@Override
	public CtcomMessage getMessage(CtcomServer server) throws OperationNotSupportedException {
		throw new OperationNotSupportedException("Cannot read data, ctcom connection not established");
	}

	@Override
	public void quit(CtcomServer server, String message) throws OperationNotSupportedException {
		Socket client = server.getClientSocket();
		try {
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));

			QuitMessage quitMessage = new QuitMessage();
			quitMessage.setMessage(message);
			
			writer.write(quitMessage.getPayload());
			writer.flush();
		} catch (IOException e) {
			// cannot open stream, or read data
			e.printStackTrace();
		}
		
		server.changeState(new ListenServerState());
	}

	@Override
	public void sendMessage(CtcomServer server, CtcomMessage message) throws OperationNotSupportedException {
		throw new OperationNotSupportedException("Cannot send ctcom data message, ctcom connection is not established");
	}

}

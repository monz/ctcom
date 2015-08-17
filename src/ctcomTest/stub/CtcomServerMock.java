package ctcomTest.stub;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import ctcom.exceptions.ReadMessageException;
import ctcom.messageImpl.ConnectMessage;
import ctcom.messageImpl.CtcomMessage;
import ctcom.messageImpl.QuitMessage;
import ctcom.messageImpl.ReadDataMessage;
import ctcom.messageTypes.MessageType;
import ctcomTest.stub.ServerMockHelper.MessageOperation;

public class CtcomServerMock implements Runnable {
	
	private int port = 4745;
	private CtcomMessage receivedMessage;
	private long timeoutSeconds = 1;
	private Semaphore messageSent = new Semaphore(0);
	private Semaphore messageReceived = new Semaphore(0);
	
	@Override
	public void run() {
		ServerSocket server = null;
		Socket client;
		
		BufferedReader reader;
		String line;
		MessageType type;
		MessageOperation operation;
		String payload;
		boolean quit = false;
		
		try {
			server = new ServerSocket(port);
			while ( ! quit ) {
				client = server.accept();
				
				reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
				line = reader.readLine();
				
				while ( line != null ) {
					// check which message type of message to receive next
					if ( isServerQuit(line) ) {
						quit = true;
						break;
					}
					
					// get message type
					type = getMessageType(line);

					// get operation
					operation = getOperation(line);
					
					// get payload
					payload = getPayload(line);
					
					switch(operation) {
						case SEND:
							sendCtcomMessage(client, type, payload);
							messageSent.release();
							break;
						case RECEIVE:
							receivedMessage = receiveCtcomMessage(client, type);
							messageReceived.release();
							break;
						case NONE:
							// fall through
							// break;
						default:
							break;
					}
					
					line = reader.readLine();
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ReadMessageException e) {
			e.printStackTrace();
		} finally {
			// close ctcom server mock
			try { server.close(); } catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private MessageType getMessageType(String line) throws ReadMessageException {
		String lineValues[] = line.split(";");
		if ( lineValues.length < 3 ) {
			throw new ReadMessageException("Malformed message, expected '<;type;CONNECT|READ_DATA|QUIT;>' but received '" + line + "'");
		}
		if ( lineValues[1].equals("type") ) {
			return MessageType.valueOf(lineValues[2]);
		}
		return null;
	}
	
	private MessageOperation getOperation(String line) throws ReadMessageException {
		String lineValues[] = line.split(";");
		if ( lineValues.length < 5 ) {
			throw new ReadMessageException("Malformed message, expected '<;...;operation;SEND|RECEIVE|NONE;>' but received '" + line + "'");
		}
		if ( lineValues[3].equals("operation") ) {
			return MessageOperation.valueOf(lineValues[4]);
		}
		return null;
	}
	
	private String getPayload(String line) throws ReadMessageException {
		String lineValues[] = line.split(";");
		String payload;
		if ( lineValues.length < 7 ) {
			throw new ReadMessageException("Malformed message, expected '<;...;payload;%s;>' but received '" + line + "'");
		}
		if ( lineValues[3].equals("operation") ) {
			payload = lineValues[6];
			// return recovered payload
			return payload.replaceAll("0x0a", "\n");
		}
		return null;
	}
	
	private boolean isServerQuit(String line) throws ReadMessageException {
		String[] lineValues = line.split(";");
		if ( lineValues.length < 3 ) {
			throw new ReadMessageException("Malformed message, expected '<;quit|type;>' but received '" + line + "'");
		}
		if ( lineValues[1].equals("quit") ) {
			return true;
		}
		return false;
	}
	
	private CtcomMessage receiveCtcomMessage(Socket client, MessageType type) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));

		String messageString = "";
		
		ExecutorService service = Executors.newSingleThreadExecutor();
		
		System.out.println("Start message");
		System.out.println("---------------");
		
		try {
			// read ctcom message, timeout after 1 second
			messageString = service.submit(new ReadCtcomMessageTask(reader)).get(timeoutSeconds, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
		
		service.shutdown();
		
		System.out.println("---------------");
		System.out.println("End message");
		
		if ( messageString.isEmpty() ) {
			return null;
		}
		
		CtcomMessage message = null;
		try {
			switch(type) {
				case CONNECT:
					message = new ConnectMessage(messageString.toString());
					break;
				case READ_DATA:
					message = new ReadDataMessage(messageString.toString());
					break;
				case QUIT:
					message = new QuitMessage(messageString.toString());
					break;
				default:
			}
		} catch(ReadMessageException e) {
			e.printStackTrace();
		}
		return message;
	}
	
	public void sendCtcomMessage(Socket client, MessageType type, String messageString) throws IOException {
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));

		CtcomMessage message = null;
		try {
			switch(type) {
				case CONNECT:
					message = new ConnectMessage(messageString);
					break;
				case READ_DATA:
					message = new ReadDataMessage(messageString);
					break;
				case QUIT:
					message = new QuitMessage(messageString);
					break;
				default:
			}
		} catch(ReadMessageException e) {
			e.printStackTrace();
		}
		
		writer.write(message.getPayload());
		writer.newLine();
		writer.flush();
	}

	public CtcomMessage getReceivedMessage() throws InterruptedException {
		messageReceived.acquire();
		return receivedMessage;
	}
}

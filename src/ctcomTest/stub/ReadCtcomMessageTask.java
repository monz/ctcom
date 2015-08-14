package ctcomTest.stub;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.concurrent.Callable;

public class ReadCtcomMessageTask implements Callable<String> {

	private String line;
	private StringBuffer messageString = new StringBuffer();
	private BufferedReader reader;
	private boolean isDone = false;
	
	public ReadCtcomMessageTask(BufferedReader reader) {
		this.reader = reader;
	}
	
	public boolean isDone() {
		return isDone;
	}

	@Override
	public String call() throws Exception {
		try {
			line = reader.readLine();
			while ( line != null ) {
				messageString.append(line);
				messageString.append(System.lineSeparator());
				System.out.println(line);
				
				// check if received entire message
				if ( line.contains(">") ) {
					break;
				}
				
				line = reader.readLine();
			}
			isDone = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return messageString.toString();
	}
}

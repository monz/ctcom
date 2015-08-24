package ctcom.util;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.naming.OperationNotSupportedException;

public class LogHelper {
	private static LogHelper instance;
	private static Logger log = Logger.getLogger(LogHelper.class.getName());
	
	private static final Level defaultLoglevel = Level.ALL;
	private static final Level defaultConsoleLoglevel = Level.WARNING;
	private static final Level defaultFileLoglevel = Level.CONFIG;
	
	private String outputFile;
	private FileHandler fh;
	private ConsoleHandler h;
	
	private LogHelper() {
		// set logger default log level
		log.setLevel(defaultLoglevel);
		
		// disable default console logger
		log.setUseParentHandlers(false);

		// init new console logger
		h = new ConsoleHandler();
		h.setLevel(defaultConsoleLoglevel);
		h.setFormatter(new SimpleFormatter());
		log.addHandler(h);
	}
	
	public static LogHelper getInstance() {
		if ( instance == null ) {
			instance = new LogHelper();
		}
		return instance;
	}
	
	public static Logger getLogger() {
		return log;
	}
	
	public void setFileLoglevel(Level level) {
		fh.setLevel(level);
	}
	
	public void setConsoleLoglevel(Level level) {
		h.setLevel(level);
	}
	
	public void setOutputFile(String path, boolean append) throws IOException, OperationNotSupportedException {
		if ( outputFile == null ) {
			outputFile = path;
		} else {
			throw new OperationNotSupportedException("Output file for logger already set to '" + outputFile + "'");
		}
		fh = new FileHandler(path, append);
		fh.setFormatter(new SimpleFormatter());
		fh.setLevel(defaultFileLoglevel);
		log.addHandler(fh);
	}
	
	public String getOutputFile() {
		return outputFile;
	}
}

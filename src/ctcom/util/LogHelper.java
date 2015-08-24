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
	
	public void setFileLoglevel(Level level) throws OperationNotSupportedException {
		if ( fh != null ) {
			fh.setLevel(level);
		} else {
			throw new OperationNotSupportedException("Cannot set file logggin level, please set output file first.");
		}
	}
	
	public void setConsoleLoglevel(Level level) {
		// never null, created in constructor
		h.setLevel(level);
	}
	
	public void setOutputFile(String pathPattern, boolean append) throws IOException, OperationNotSupportedException {
		if ( outputFile == null ) {
			outputFile = pathPattern;
		} else {
			throw new OperationNotSupportedException("Output file for logger already set to '" + outputFile + "'");
		}
		fh = new FileHandler(pathPattern, append);
		fh.setFormatter(new SimpleFormatter());
		fh.setLevel(defaultFileLoglevel);
		log.addHandler(fh);
	}
	
	public String getOutputFile() {
		return outputFile;
	}
}

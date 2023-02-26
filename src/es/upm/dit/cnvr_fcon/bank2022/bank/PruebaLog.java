package es.upm.dit.cnvr_fcon.bank2022.bank;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PruebaLog {

	public static Logger LOGGER = Logger.getLogger(PruebaLog.class.getName());
	
	public PruebaLog() {
		configureFile();
	}
	
	private void configureLogger() {
		System.setProperty("java.util.logging.config.file", "/Users/aalonso/Desktop/logging.properties");
		LOGGER = Logger.getLogger(MainBank.class.getName());
		LOGGER.setLevel(Level.FINEST); //FINEST
	}

	private void configureLogger1() {
		//ConsoleHandler handler = new ConsoleHandler();
		//handler.setLevel(Level.FINEST); //FINEST
		//LOGGER.addHandler(handler);
		LOGGER.setLevel(Level.INFO); //FINEST		
	}	
	
	private void configureFile() {
		try {
//			ConsoleHandler handler = new ConsoleHandler();
			FileHandler fileHandler = new FileHandler("/Users/aalonso/log/zk.log");
//			handler.setLevel(Level.FINEST);
//			LOGGER.addHandler(handler);
			LOGGER.addHandler(fileHandler);
			LOGGER.setLevel(Level.INFO);

		} catch (SecurityException e) {  
			e.printStackTrace();  
		} catch (IOException e) {  
			e.printStackTrace();  
		}  
	}
	
	public static void main(String[] args) {
		System.out.println("Hola");
		
		PruebaLog pruebaLog = new PruebaLog();
		
		LOGGER.fine("Fino");
		LOGGER.info("Info");
		LOGGER.severe("Severo");
	}
	
}

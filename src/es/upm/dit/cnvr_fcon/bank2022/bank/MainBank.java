package es.upm.dit.cnvr_fcon.bank2022.bank;


import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
//import java.util.logging.Filter;
//import java.util.logging.Handler;
import java.util.logging.Level;
//import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.apache.zookeeper.KeeperException;

import es.upm.dit.cnvr_fcon.bank2022.common.Client;
import es.upm.dit.cnvr_fcon.bank2022.interfaces.*;
//import es.upm.dit.cnvr_fcon.bank2022.library.BankManager;
//import es.upm.dit.cnvr_fcon.bank2022.library.Operations;

import es.upm.dit.cnvr_fcon.bank2022.tobedone.*;

/**
 * This is the main in the application. The code that is executed 
 * for providing the application.
 * 
 * Its main duties is to create a ManagerBank. It also provides the API
 * for clients
 *
 * @authors Luis Alberto López Álvarez y Álvaro de Rojas Maraver
 * @date 20230115
 */
public class MainBank {

	private Random random = new Random();
  
	static {
		System.setProperty("java.util.logging.SimpleFormatter.format",
				"[%1$tF %1$tT][%4$-7s] [%5$s] [%2$-7s] %n");

		//    		"[%1$tF %1$tT] [%2$-7s] %3$s %n");
		//           "[%1$tF %1$tT] [%4$-7s] %5$s %n");
		//   "%4$s: %5$s [%1$tc]%n");
		//    "%1$tb %1$td, %1$tY %1$tl:%1$tM:%1$tS %1$Tp %2$s%n%4$s: %5$s%n");
	}

	public static Logger LOGGER = Logger.getLogger(MainBank.class.getName());
  
	public MainBank() {
		configureLogger1();
	}

	//////////////////////////////////////////////////////////////////////////

	// Three options are provided for configuring the logs.
	// Select the best option. Ensure that the paths are valid
	
	private void configureLogger() {
		System.setProperty("java.util.logging.config.file", "/Users/aalonso/Desktop/logging.properties");
		LOGGER = Logger.getLogger(MainBank.class.getName());
		LOGGER.setLevel(Level.FINEST); //FINEST
	}

	private void configureLogger1() {
		ConsoleHandler handler = new ConsoleHandler();
		handler.setLevel(Level.FINEST); //FINEST
		LOGGER.addHandler(handler);
		LOGGER.setLevel(Level.FINEST); //FINEST		
	}	
	
	private void configureFile() {
		try {
			ConsoleHandler handler = new ConsoleHandler();
			FileHandler fileHandler = new FileHandler("/Users/aalonso/log/zk.log");
			handler.setLevel(Level.INFO);
			LOGGER.addHandler(handler);
			LOGGER.addHandler(fileHandler);
			LOGGER.setLevel(Level.INFO);

		} catch (SecurityException e) {  
			e.printStackTrace();  
		} catch (IOException e) {  
			e.printStackTrace();  
		}  
	}
	//////////////////////////////////////////////////////////////////////////

	private void initMembers(Operations operations) throws InterruptedException {

		//if (!dht.containsKey("Angel")) {
		operations.put(new Client(1, "Angel", 10));
		//}
		//if (!dht.containsKey("Bernardo")) {
		operations.put(new Client(2, "Bernardo", 20));
		//}
		//if (!dht.containsKey("Carlos")) {
		operations.put(new Client(3, "Carlos", 30));
		//}
		//if (!dht.containsKey("Daniel")) {
		operations.put(new Client(4, "Daniel", 40));
		//}
		//if (!dht.containsKey("Eugenio")) {
		operations.put(new Client(5, "Eugenio", 50));
		//}
		//if (!dht.containsKey("Zamorano")) {
		operations.put(new Client(6, "Zamorano", 60));
		//}
	}

	//////////////////////////////////////////////////////////////////////////


	private Client readClient(Scanner sc) {
		int accNumber = 0;
		String name   = null;
		int balance   = 0;

		System. out .print(">>> Enter account number (int) = ");
		if (sc.hasNextInt()) {
			accNumber = sc.nextInt();
		} else {
			System.out.println("The provised text provided is not an integer");
			sc.next();
			return null;
		}

		System. out .print(">>> Enter name (String) = ");
		name = sc.next();

		System. out .print(">>> Enter balance (int) = ");
		if (sc.hasNextInt()) {
			balance = sc.nextInt();
		} else {
			System.out.println("The provised text provided is not an integer");
			sc.next();
			return null;
		}
		return new Client(accNumber, name, balance);
	}
	//////////////////////////////////////////////////////////////////////////

	private void put(Client map, Operations operations) throws InterruptedException {
		operations.put(map);
	}

	//////////////////////////////////////////////////////////////////////////

	private Integer get(int accNumber, Operations operations) throws InterruptedException {

		Integer value  = operations.get(accNumber);

		return value;

	}
	
	//////////////////////////////////////////////////////////////////////////

	private Integer remove(int accNumber, Operations operations) throws InterruptedException {

		Integer value  = operations.remove(accNumber);

		return value;

	}

	//////////////////////////////////////////////////////////////////////////

	private Integer update(int accNumber, int balance, Operations operations) throws InterruptedException {

		Integer value  = operations.update(accNumber, balance);

	    return value;
	}
	
	public static void main(String[] args) throws KeeperException, InterruptedException, IOException {

		boolean correct = false;
		int     menuKey = 0;
		boolean exit    = false;
		Scanner sc      = new Scanner(System.in);
		Client  client;
		Integer value   = 0;

		MainBank       mainBank      = new MainBank();
		BankManager    bankManager  = new BankManager(mainBank.random.nextInt(100));

		//If there is already a quorum
		if (!bankManager.isCreated()) {
			System.out.println("Bye. There is already a quorum");
			return;
		}
		
		
		Operations     operations   = bankManager.getOperations();
		int accNumber = 0;

		while (!exit) {
			try {
				correct = false;
				menuKey = 0;
				while (!correct) {
					System. out .println(">>> Enter option: 1) Put. 2) Get. 3) Remove. 4) Update  5) Values 7) Init 0) Exit");				
					if (sc.hasNextInt()) {
						menuKey = sc.nextInt();
						correct = true;
					} else {
						sc.next();
						System.out.println("The provised text provided is not an integer");
					}

				}
				
				if (!bankManager.isQuorum()) {
					System.out.println("No hay quorum. No es posible ejecutar su elección");
					continue;
				}

				switch (menuKey) {
				case 1: // Put
					LOGGER.finest("Main: put");
					client = mainBank.readClient(sc);
					mainBank.put(client, operations);
					break;

				case 2: // Get
					System. out .print(">>> Enter account number (int) = ");
					if (sc.hasNextInt()) {
						accNumber = sc.nextInt();
						value  = mainBank.get(accNumber, operations);
						if (value != null) {
							System.out.println(value);							
						} else {
							System.out.println("The key: " + accNumber + " does not exist");
						}
					} else {
						System.out.println("The provised text provided is not an integer");
						sc.next();
					}									
					break;
				case 3: // Remove
					System. out .print(">>> Enter account number (int) = ");
					if (sc.hasNextInt()) {
						accNumber = sc.nextInt();
						value  = mainBank.remove(accNumber, operations);
						if (value != null) {
							System.out.println("The key: " + accNumber + " has not deleted succesfully");
						} else {
							System.out.println("The key: " + accNumber + " has been deleted");							

						}
					} else {
						System.out.println("The provised text provided is not an integer");
						sc.next();
					}									
					break;
				case 4: // Update
					int balance = 0;
					System. out .print(">>> Enter account number (int) = ");
					if (sc.hasNextInt()) {
						accNumber = sc.nextInt();
					} else {
						System.out.println("The provised text provided is not an integer");
						sc.next();
					}
					System. out .print(">>> Enter balance (int) = ");
					if (sc.hasNextInt()) {
						balance = sc.nextInt();
					} else {
						System.out.println("The provised text provided is not an integer");
						sc.next();
					}
					value  = mainBank.update(accNumber, balance, operations);
					break;
				case 5:
					//ArrayList<Integer> list = new ArrayList<Integer>();
					System.out.println("List of values in the bank:");
					System.out.println(bankManager.clientDBString());
					break;
				case 6:
					System.out.println("The option is not available");
					break;
				case 7:
					mainBank.initMembers(operations);
					break;
				case 0:
					exit = true;	
				default:
					break;
				}
			} catch (Exception e) {
				LOGGER.severe("Exception at Main. Error read data");
				e.printStackTrace();
			}
		}
		sc.close();
	}
}
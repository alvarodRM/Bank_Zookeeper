package es.upm.dit.cnvr_fcon.bank2022.common;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import es.upm.dit.cnvr_fcon.bank2022.bank.MainBank;


/**
 * The dababase in the bank with the clients
 * @author aalonso
 */
public class ClientDB implements Serializable {

	private java.util.logging.Logger LOGGER = MainBank.LOGGER;
	
	private static final long serialVersionUID = 1L;

	private java.util.HashMap <Integer, Client> clientDB; 

	/**
	 * Start a dababase of clients, starting from one existing
	 * @param clientDB the database of clients
	 */
	public ClientDB (ClientDB clientDB) {
		this.clientDB = clientDB.getClientDB();
	}

	/**
	 * Start an empty database of clients
	 */
	public ClientDB() {
		clientDB = new java.util.HashMap <Integer, Client>();
	}

	/**
	 * Get the datababase of clients
	 * @return the database
	 */
	public java.util.HashMap <Integer, Client> getClientDB() {
		return this.clientDB;
	}

	/**
	 * Includes a client in the database. 
	 * @param client the client
	 * @return true, if the client has been included successfully. 
	 * false, if the client exists already in the database
	 */
	public boolean createClient(Client client) {		
		if (clientDB.containsKey(client.getAccountNumber())) {
			return false;
		} else {
			clientDB.put(client.getAccountNumber(), client);
			return true;
		}		
	}

	/**
	 * Get a client in the databse
	 * @param accountNumber the account number of the client
	 * @return the client. If the client does not exists in the
	 * database, null is returned
	 */
	public Client readClient(Integer accountNumber) {
		if (clientDB.containsKey(accountNumber)) {
			return clientDB.get(accountNumber);
		} else {
			return null;
		}		
	}

	/**
	 * Update the balance of a client in the database.
	 * @param accNumber the account number of the client
	 * @param balance the balanced
	 * @return true if the bajlance has been updated successfully.
	 * false, if the client does not exist in the database 
	 */
	public boolean updateClient (int accNumber, int balance) {
		if (clientDB.containsKey(accNumber)) {
			Client client = clientDB.get(accNumber);
			client.setBalance(balance);
			clientDB.put(client.getAccountNumber(), client);
			return true;
		} else {
			return false;
		}	
	}

	/**
	 * Delete a client in the database.
	 * @param accountNumber the account number of the client 
	 * @return the client deleted. null, if the client does not 
	 * exist in the database 
	 */
	public Client deleteClient(Integer accountNumber) {
		if (clientDB.containsKey(accountNumber)) {
			return clientDB.remove(accountNumber);
		} else {
			return null;
		}	
	}

	/**
	 * Change the current database
	 * @param clientDB the new database
	 * @return true if the database has been processed successfully.
	 * null, if the database is not valid
	 */
	public boolean createBank(ClientDB clientDB) {

		if (clientDB != null) {
			System.out.println("createBank");
			this.clientDB = clientDB.getClientDB();
			System.out.println(clientDB.toString());
			return true;			
		} else {
			return false;
		}
	}

	public boolean saveDBFile(String filepath) {
		try {
			FileOutputStream fileOut = new FileOutputStream(filepath);
			ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
			objectOut.writeObject(clientDB);
			objectOut.close();
			LOGGER.fine("The Object  was succesfully written to a file");
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			LOGGER.severe("Unexpected file");
			return false;
		}
	}
	
	public boolean getDBFile (String filepath) {
		
		try {
			FileInputStream fileIn = new FileInputStream(filepath);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);            
            this.clientDB = (java.util.HashMap <Integer, Client>) objectIn.readObject();
            LOGGER.fine("The Object has been read from the file");
            objectIn.close();
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			LOGGER.severe("Unexpected file");
			return false;
		}		
	}
	
	public String toString() {
		String aux = new String();

		for (java.util.HashMap.Entry <Integer, Client>  entry : clientDB.entrySet()) {
			aux = aux + entry.getValue().toString() + "\n";
		}
		return aux;
	}
	public static void main(String[] args) {

		Client client  = new Client(1, "Alonso", 1000);
		Client client1 = new Client(2, "Barco",  2000);
		ClientDB clientDB = new ClientDB();
		clientDB.createClient(client);
		clientDB.createClient(client1);
		System.out.println(clientDB.toString());
		
		String filepath = "/tmp/clientdb";
		clientDB.saveDBFile(filepath);
		clientDB.getDBFile(filepath);
		System.out.println(clientDB.toString());	
	}	
}




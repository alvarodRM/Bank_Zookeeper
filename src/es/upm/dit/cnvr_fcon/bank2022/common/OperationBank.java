package es.upm.dit.cnvr_fcon.bank2022.common;

import java.io.Serializable;

/**
 * This class allows to encapsulate operations on an object.
 * Its intended for storing and sending with znodes
 * @author aalonso
 *
 */
public class OperationBank implements Serializable {

	private static final long serialVersionUID = 1L;
	private OperationEnum operation;
	private Client        client        = null;
	private Integer       accountNumber = 0;
	private ClientDB      clientDB      = null;
	
	// When the node is going to be sent, the node must include the channel equal to null.
	// The relevant should be the address
	
   // ADD_CLIENT, UPDATE_CLIENT
	/**
	 * Encapsulate an operation for updating and creating a client
	 * @param operation the type of operation 
	 * @param client the client
	 */
	public OperationBank (OperationEnum operation,
			            Client client) {
		this.operation = operation;
		this.client    = client;
	}
	
	// READ_CLIENT, DELETE_CLIENT
	/**
	 * Encapsulate an operation for updating and creating a client
	 * @param operation the type of operation 
	 * @param accountNumber the account number
	 */
	public OperationBank (OperationEnum operation,
						 Integer accountNumber ) {
		this.operation     = operation;
		this.accountNumber = accountNumber;
	}

	/**
	 * Encapsulate an operation for  a client
	 * @param operation the type of operation 
	 * @param clientDB
	 */
//	public OperationBank (OperationEnum operation, 
//						 ClientDB clientDB) {
//		this.operation = operation;
//		this.clientDB  = clientDB;
//	}
	
	/**
	 * Get the type of an operation
	 * @return the type
	 */
	public OperationEnum getOperation() {
		return operation;
	}

	/**
	 * Set the type of an operation
	 * @param operation the type
	 */
	public void setOperation(OperationEnum operation) {
		this.operation = operation;
	}

	/**
	 * Get the client in a bank operation 
	 * @return the client
	 */
	public Client getClient() {
		return client;
	}

	/**
	 * Set the client of a bank operation
	 * @param client the client
	 */
	public void setClient(Client client) {
		this.client = client;
	}

	/**
	 * Get the account number in a bank operation 
	 * @return the account number
	 */
	public Integer getAccountNumber() {
		return accountNumber;
	}

	/**
	 * Set the the account number of a bank operation
	 * @param accountNumber the account number
	 */
	public void setAccountNumber(Integer accountNumber) {
		this.accountNumber = accountNumber;
	}
	
//	public ClientDB getClientDB() {
//		return clientDB;
//	}

//	public void setClientDB(ClientDB clientDB) {
//		this.clientDB = clientDB;
//	}
	
	@Override
	public String toString() {
		
		String string = null;
		
		string = "OperationBank [operation=" + operation;
		if (client != null) string = string + ", client=" + client.toString();
		string = string + ", accountNumber=" + accountNumber + "]\n";
		if (clientDB != null) string = string + clientDB.toString();
		
		return string;
	}
}

package es.upm.dit.cnvr_fcon.bank2022.common;


import es.upm.dit.cnvr_fcon.bank2022.bank.MainBank;

/**
 *
 * Receives an operation (OperationBank) an processes it
 *
 * @author aalonso
 * @since 2022/10/17
 */
public class ServicesBank {

	private java.util.logging.Logger LOGGER = MainBank.LOGGER;

	private ClientDB clientDB;

	/**
	 * The constructor
	 * @param clientDB the database to be processed
	 */
	public ServicesBank(ClientDB clientDB) {
		this.clientDB = clientDB;
	}

	/**
	 * Operation for processing an operation
	 * @param data the operation. Its type is an array of bytes
	 * @return the client that been processed. null if the operation 
	 * was not feasible
	 */
	public Client processOpn(byte[] data) {
		Client client;
		OperationBank operation = ConvertsByte.ByteToOperation(data);
		
		if (operation == null) {
			return null;		
		}
		
		try {
			switch (operation.getOperation()) {
			case CREATE_CLIENT: // Put
				LOGGER.finest("Operation PUT");

				clientDB.createClient(operation.getClient());
				return operation.getClient();
				//break;
			case READ_CLIENT: // Get
				LOGGER.finest("Operation GET");
				Integer accountNumber = operation.getAccountNumber();
				return clientDB.readClient(accountNumber);			
				//break;
				
			case UPDATE_CLIENT: // Update
				LOGGER.finest("Operation UPDATE");
				client = operation.getClient();
				Boolean status = clientDB.updateClient(
						client.getAccountNumber(), client.getBalance());
				if (status) {
					return client;
				} else {
					return null;
				}

				//break;
			case DELETE_CLIENT: // Delege
				LOGGER.finest("Operation DELETE");
				clientDB.deleteClient(operation.getAccountNumber());
				return null;
			case CREATE_BANK:
				return null;
			}
		} catch (Exception E) {
			LOGGER.severe("Unexpected expcetion");
			return null;
		}
		return null;


	}

}

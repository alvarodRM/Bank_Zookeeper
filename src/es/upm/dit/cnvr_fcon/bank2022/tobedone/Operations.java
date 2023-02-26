package es.upm.dit.cnvr_fcon.bank2022.tobedone;

import es.upm.dit.cnvr_fcon.bank2022.bank.MainBank;
import es.upm.dit.cnvr_fcon.bank2022.common.*;
import es.upm.dit.cnvr_fcon.bank2022.interfaces.OperationInterface;
import es.upm.dit.cnvr_fcon.bank2022.interfaces.SendMessages;

/**
 * 
 * This class provides a set of operations for managing a client.
 * It is aim is communicate the operation for the all servers. 
 * When a client invokes an operation, it is sent to the ZooKeeper ensemble 
 * letting all replicas  to get it.
 * 
 * The operations are not directly processed. Only them can run according 
 * to the order that the operations has been invoked to the ZooKeeper ensemble
 * 
 * Operations are synchronous. The client cannot invoke an operation when the
 * previous has been already processed. Then, the invocation is blocked until
 * the operation has been processed. 
 * 
 * @authors Luis Alberto López Álvarez y Álvaro de Rojas Maraver
 */
public class Operations implements OperationInterface{

	private SendMessages sendMsgs;	
	private ClientDB clientDB;
	private PendingOpns pendingOpns;
	
	public Operations(SendMessages sendMsgs, ClientDB clientDB, PendingOpns pendingOpns) {
		// TO DO 
        this.sendMsgs = sendMsgs;
        this.clientDB = clientDB;
        this.pendingOpns = pendingOpns;
		// FIN TO DO
	}
	
	// TO DO
	public boolean validate(Integer accNumber, String addr, Client client) {
		if (addr != null && accNumber != null && client != null) {
			return true;
		} else {
			return false;
		}
	}
	// FIN TODO
	
	public Client put (Client client) throws InterruptedException {
		// TO DO
		String addr = sendMsgs.sendAdd(client);
        if (addr != null && client != null) {
        	pendingOpns.putOpn(addr);
        	return client;
        } else {
        	return null;
        }
		// FIN TO DO
	}

	public Integer get (Integer accNumber) throws InterruptedException {
		// TO DO
		String addr = sendMsgs.sendRead(accNumber);
		Client client = clientDB.readClient(accNumber);
        if (validate(accNumber,addr, client)) {
            pendingOpns.putOpn(addr);
            return client.getBalance();
        } else {
        	return null;     
        }
		// FIN TO DO
	}
	
	public Integer remove(Integer accNumber) throws InterruptedException {
		// TO DO
		String addr = sendMsgs.sendDelete(accNumber);
		Client client = clientDB.readClient(accNumber);
        if (validate(accNumber,addr, client)) {
            pendingOpns.putOpn(addr);
            return client.getAccountNumber();
        } else {
        	return null;
        }
		// FIN TO DO
	}

	public Integer update(Integer accNumber, Integer balance) throws InterruptedException {
		// TO DO
        Client client = new Client(accNumber, "", balance);
        String addr = sendMsgs.sendUpdate(client);
        Client newClient = clientDB.readClient(accNumber);
        if ((validate(accNumber,addr, client)) && (newClient.getBalance() != client.getBalance())) {
            pendingOpns.putOpn(addr);
            return newClient.getBalance();
        } else {
        	return null;
        }
		// FIN TO DO
	}

}

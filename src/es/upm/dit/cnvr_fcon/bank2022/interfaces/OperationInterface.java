package es.upm.dit.cnvr_fcon.bank2022.interfaces;

import es.upm.dit.cnvr_fcon.bank2022.common.Client;

public interface OperationInterface {

	public Client put (Client client) throws InterruptedException;
	
	public Integer get (Integer accNumber) throws InterruptedException;

	public Integer remove(Integer accNumber) throws InterruptedException;

	public Integer update(Integer accNumber, Integer balance) throws InterruptedException;

	//public Integer GetClientDB();

}
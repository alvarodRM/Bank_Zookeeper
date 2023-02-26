package es.upm.dit.cnvr_fcon.bank2022.interfaces;

import es.upm.dit.cnvr_fcon.bank2022.common.Client;
import es.upm.dit.cnvr_fcon.bank2022.common.ClientDB;

public interface SendMessages {

	public String sendAdd(Client client);

	public String sendRead(Integer accountNumber);

	public String sendUpdate(Client client);

	public String sendDelete(Integer accountNumber);
	
	public String sendCreateBank (ClientDB clientDB);

}

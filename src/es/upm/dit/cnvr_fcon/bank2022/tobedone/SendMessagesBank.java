package es.upm.dit.cnvr_fcon.bank2022.tobedone;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import es.upm.dit.cnvr_fcon.bank2022.common.Client;
import es.upm.dit.cnvr_fcon.bank2022.common.ClientDB;
import es.upm.dit.cnvr_fcon.bank2022.common.ConvertsByte;
import es.upm.dit.cnvr_fcon.bank2022.common.CreateNode;
import es.upm.dit.cnvr_fcon.bank2022.common.OperationBank;
import es.upm.dit.cnvr_fcon.bank2022.common.OperationEnum;
import es.upm.dit.cnvr_fcon.bank2022.interfaces.SendMessages;

/**
 * It sends an operation. In particular, serialize an operation,
 * creates a znode in the proper operations tree, stores the operation 
 * in the znode and set the required watcher
 * 
 * @authors Luis Alberto López Álvarez y Álvaro de Rojas Maraver
 */
public class SendMessagesBank implements SendMessages {

	private ZooKeeper zk;
	private String path;
	private String child;
	private CreateMode mode;
	private CreateNode createNode;
	private Watcher watcher;
	private ConvertsByte convertsByte;
	private String nodeId;

	public SendMessagesBank(ZooKeeper zk, String path, String child, Watcher watcher)  
	{
		this.zk           = zk;
		this.path         = path;
		this.child        = child;
		this.mode         = CreateMode.PERSISTENT_SEQUENTIAL;
		this.createNode   = new CreateNode();
		this.watcher      = watcher;
		this.convertsByte = new ConvertsByte();
	}

	private String sendMessage(OperationBank operation) {
		// TO DO
		byte[] bytes_op;
		if (operation == null) {
			return null;
	    } else {
	    	bytes_op = ConvertsByte.OperationToByte(operation);
	        nodeId = createNode.createNode(zk, path, child, mode, bytes_op, watcher);
	        return nodeId;
		}
		// FIN TO DO
	}

	public String sendAdd(Client client) {
		OperationBank operation = new OperationBank(OperationEnum.CREATE_CLIENT, client);
		return sendMessage(operation);
	}

	public String sendRead(Integer accountNumber) {
		OperationBank operation = new OperationBank(OperationEnum.READ_CLIENT, accountNumber);
		return sendMessage(operation);
	}

	public String sendUpdate(Client client) {
		OperationBank operation = new OperationBank(OperationEnum.UPDATE_CLIENT, client);
		return sendMessage(operation);
	}

	public String sendDelete(Integer accountNumber) {
		OperationBank operation = new OperationBank(OperationEnum.DELETE_CLIENT, accountNumber);
		return sendMessage(operation);
	}

	public String sendCreateBank (ClientDB clientDB) {
		return null;
	}
	//	public String sendCreateBank (ClientDB clientDB) {
	//		OperationBank operation = new OperationBank(OperationEnum.CREATE_BANK, clientDB);
	//		return sendMessage(operation);
	//	}
}

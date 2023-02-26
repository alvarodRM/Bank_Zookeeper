package es.upm.dit.cnvr_fcon.bank2022.tobedone;

import java.util.Iterator;
import java.util.List;
import java.util.Collections;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import es.upm.dit.cnvr_fcon.bank2022.bank.MainBank;
import es.upm.dit.cnvr_fcon.bank2022.common.*;

/**
 * 
 * This class implements Watcher. It is intented to receive the 
 * operations pending to be done. It is associated to a zNode. 
 * When its children is created or deleted, a wather should be received.
 * 
 * @authors Luis Alberto López Álvarez y Álvaro de Rojas Maraver
 */

public class OperationWatcher implements Watcher
{
	private java.util.logging.Logger LOGGER = MainBank.LOGGER;
	private ZooKeeper zk;
	private BankManager managerBank;
	private String opns;
	private ServicesBank servicesBank;
	private PendingOpns pendingOpns;
	private String memberID;
	private String lastOperation = "";
    
	// TO DO
	public OperationWatcher(ZooKeeper zk, BankManager managerBank, String opns, ClientDB clientDB, PendingOpns pendingOpns, String memberID) throws KeeperException, InterruptedException {
		this.zk = zk;
	    this.managerBank = managerBank;
	    this.opns = opns;
	    zk.getChildren(this.opns, this);
	    servicesBank = new ServicesBank(clientDB);
	    this.pendingOpns = pendingOpns;
	    this.memberID = memberID;
	}
    
	// Tecnica recursiva para eliminar los nodos hijos antes de borrar el nodo padre.
    private boolean eliminarOperacion(String opn, Watcher watcher) throws KeeperException, InterruptedException {
    	List<String> children = zk.getChildren(opn, false);
    	for (String child: children) {
    		String operacion = opn + "/" + child;
            if (zk.exists(operacion, false) != null) {
            	zk.delete(operacion, zk.exists(operacion, false).getVersion());
            }
    	}
    	zk.delete(opn, zk.exists(opn, true).getVersion());
        return true;
    }
    
    private boolean existeQuorum(String operation) throws KeeperException, InterruptedException {
        // Obtiene la lista de miembros que ha procesado la aplicacion
    	List<String> membersList = zk.getChildren(operation, this);
        return membersList.size() + 1 >= managerBank.getQuorum();
    }
    
    private void processOperation(String currentOperation, Stat operationStatus, List<String> pendingOperations) throws KeeperException, InterruptedException {
    	// Actualiza la ultima operacion procesada
	    lastOperation = pendingOperations.get(0);
	    // Procesa la operacion
	    servicesBank.processOpn(zk.getData(currentOperation, true, operationStatus));
	    pendingOpns.unlockOpn(currentOperation);
	    // Verifica si hay quorum para eliminar la operacion
	    if (existeQuorum(currentOperation)) {
	        eliminarOperacion(currentOperation, this);
	    }
	    else {
	    	// Crea un nuevo nodo en zk
	        CreateNode create = new CreateNode();
	        create.createNode(zk, opns + "/" + pendingOperations.get(0), "/" + memberID, CreateMode.PERSISTENT, new byte[0], null);
	    }
    }
    // FIN TO DO
    
    public void process(WatchedEvent event) {
        // TO DO
    	try {
    		// Verifica si tiene una operacion pendiente
            Stat operationStatus = zk.exists(opns, false);
            // Obtiene la lista de operaciones pendientes
            List<String> pendingOperations = zk.getChildren(opns, this);
            if (pendingOperations == null || pendingOperations.isEmpty()) {
                return;
            }
            int index = 0;
            // Bucle que se realiza mientras haya operaciones pendientes
            while (index < pendingOperations.size()) {
	            String currentOperation = opns + "/" + pendingOperations.get(0);
	            if (lastOperation.equals(pendingOperations.get(0))) {
	                if (existeQuorum(currentOperation)) {
	                    eliminarOperacion(currentOperation, this);
	                }
	                return;
	            }
	            processOperation(currentOperation, operationStatus, pendingOperations);
	            index ++;
            }
           
        }
        catch (Exception e) {
        	LOGGER.severe("Excepcion en el metodo process de OperationWatcher");
        }
    	// TO DO
    }
    
    private void printListMembers(final List<String> list) {
        this.LOGGER.fine("Remaining # members:" + list.size());
        String string = "";
        final Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            string = String.valueOf(string) + iterator.next() + " ,";
        }
        this.LOGGER.fine(string);
    }
}
package es.upm.dit.cnvr_fcon.bank2022.tobedone;

import es.upm.dit.cnvr_fcon.bank2022.bank.*;
import es.upm.dit.cnvr_fcon.bank2022.common.*;
import es.upm.dit.cnvr_fcon.bank2022.interfaces.*;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;

import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * This class creates and initiate the main clases in the system. 
 * In addition, it connectates to the ZooKeeper ensemble and
 * creates the basic zNodes required
 *
 * @authors Luis Alberto López Álvarez y Alvaro de Rojas Maraver
 */
public class BankManager implements Watcher{
	
	private static final int SESSION_TIMEOUT = 5000;
	private static final int CONNECTION_TIMEOUT = 2000;

	//TODO Las tres son de ExploreZk
	private static Integer mutex        = -1;
	private static Integer mutexBarrier = -2;
	private static Integer mutexMember  = -3;
	private static int nMembers  		= 0;

	private static String rootMembers = "/members";
	private static String member      = "/member-";

	private String opns = "/opns";
	private String opn  = "/opn-";

	private java.util.logging.Logger LOGGER = MainBank.LOGGER;
	private int quorum = 3;
	private String memberID;

	// TO DO
	private ZooKeeper zk;
	private ClientDB clientDB;
	private String db = "/bdtrans";
        private OperationWatcher operationWatcher;
        private PendingOpns pendingOpns;
        private Operations operations;
        private QuorumManagerFT quorumManagerFT;
        private boolean existeMiembro;
	// FIN TO DO
    
    public BankManager(int serverID) throws KeeperException, InterruptedException, IOException {
		// TO DO
        existeMiembro = false;
        clientDB = new ClientDB();
        configureZK();
		// FIN TO DO
	}
    
    public void configureZK() throws KeeperException, InterruptedException, IOException {
		// TO DO
		conectarZooKeepper();
        crearNodoyComprobarQuorum();
        pendingOpns = new PendingOpns();
        operationWatcher = new OperationWatcher(zk, this, opns, clientDB, pendingOpns, memberID);
        operations = new Operations(new SendMessagesBank(zk, opns, opn, operationWatcher), clientDB, pendingOpns);
        new MembersWatcher(zk, rootMembers, quorumManagerFT, memberID, clientDB, db);
        getClientDB();
		// FIN TO DO
	}
    
    // TO DO
    public void conectarZooKeepper() throws IOException, KeeperException, InterruptedException {
    	String[] hosts = {"127.0.0.1:2181", "127.0.0.1:2181"};
		int i = (int) (Math.random() * hosts.length);
        new CreateNode();
        if (zk == null) {
        	zk = new ZooKeeper(hosts[i], 5000, this);
        	synchronized (mutex) {
        		mutex.wait();
        	}
            zk.exists("/", false);
        }
    }
    
    public void crearNodoyComprobarQuorum() throws KeeperException, InterruptedException {
        quorumManagerFT = new QuorumManagerFT(quorum);
        String[] paths = {rootMembers, opns, db};
        for (String path : paths) {
            if (zk.exists(path, false) == null) {
                zk.create(path, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
        }
        if (zk.getChildren(rootMembers, operationWatcher).size() == quorum) {
            return;
        }
        memberID = zk.create(rootMembers + member, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        memberID = memberID.replace(rootMembers + "/", "");
        existeMiembro = true;
        printListMembers(zk.getChildren(rootMembers, false, zk.exists(rootMembers, false)));
    }
    
 	public void getClientDB() throws KeeperException, InterruptedException {
 			List<String> children = zk.getChildren(db, this);
             if ((children.size() == 1) && (zk.exists(db + "/" + children.get(0), false) != null)) {
                 clientDB.getDBFile(ConvertsByte.ByteToString(zk.getData(db + "/" + children.get(0), false, zk.exists(db + "/" + children.get(0), false))));
                 zk.delete(db + "/" + children.get(0), zk.exists(db + "/" + children.get(0), false).getVersion());
             }
     }
     
 	//Metodo referenciado desde el MainBank, para validar la existencia de Quorum
     public boolean isCreated() {
         return existeMiembro;
     }
     
     public Operations getOperations() {
     	if (operations!=null) {
     		return operations;
     	} else {
     		return null;
     	}
     }
 	// FIN TO DO

 	@Override
 	public void process(WatchedEvent event) {
 		try {
 			if (event.getPath() == null) {			
 				LOGGER.finest("SyncConnected");
 				synchronized (mutex) {
 					mutex.notify();
 				}
 			}
 		} catch (Exception e) {
 			LOGGER.severe("Unexpected Exception process Watcher PROCESS");
 		}
 	}

 	public boolean isQuorum () {
 		// TO DO
         return quorumManagerFT.isQuorum();
 		// FIN TO DO
 	}

 	public int getQuorum() {
 		return this.quorum;
 	}

 	public String clientDBString () {
 		// TO DO
 		String cliente = clientDB.toString();
 		if (cliente != null) {
 			return cliente;
 		} else {
 			return null;
 		}
 		// FIN TO DO
 	}

 	private void printListMembers (List<String> list) {
 		LOGGER.fine("Remaining # members:" + list.size());
 		String string = "";
 		for (Iterator<String> iterator = list.iterator(); iterator.hasNext();) {
 			string = string + (String) iterator.next() + " ,";
 		}
 		LOGGER.fine(string);				
 	}
 }

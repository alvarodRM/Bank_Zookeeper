package es.upm.dit.cnvr_fcon.bank2022.tobedone;

import java.util.Iterator;
import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooDefs.*;
import org.apache.zookeeper.data.Stat;

import es.upm.dit.cnvr_fcon.bank2022.bank.MainBank;
import es.upm.dit.cnvr_fcon.bank2022.common.ConvertsByte;
import es.upm.dit.cnvr_fcon.bank2022.common.ClientDB;
/**
 * 
 * This class implements Watcher. It is intended to receive the 
 * membership state. It is associated to a zNode. 
 * When its children is created or deleted, a watcher should be received.
 * 
 * @authors Luis Alberto López Álvarez y Álvaro de Rojas Maraver
 */
public class MembersWatcher implements Watcher{
	private ZooKeeper zk;
	private String rootMembers;
	private int nMembers;
	// No es imprescindible usar QuorurmManager
	private QuorumManagerFT quorumManager;
	private String memberID;
	private ClientDB clientDB;
	// TO DO
	private String data;
	private String ruta = "/tmp/clientdb";
	// FIN TO DO

	public MembersWatcher(ZooKeeper zk, String rootMembers, QuorumManagerFT quorumManager, String memberID, ClientDB clientDB, String data) throws KeeperException, InterruptedException {
		// TO DO
		List<String> miembros = zk.getChildren(rootMembers, this);
	    this.zk = zk;
	    this.rootMembers = rootMembers;
	    nMembers = 0;
	    this.quorumManager = quorumManager;
	    this.memberID = memberID;
	    this.clientDB = clientDB;
	    this.data = data + "/db";
	    quorumManager.setnServers(miembros.size());
		// FIN TO DO
	}
	
	// TO DO
	public void actualizarBBDD(WatchedEvent event, List<String> miembros) throws KeeperException, InterruptedException {
		if (event != null && fijaLider(miembros) == true && quorumManager.dbPendiente() == true) {
        	guardarDB(miembros);
        } 
        else if (event != null && fijaLider(miembros) == true){
            quorumManager.dbActualizada();
        } else {
        	return;
        }
	}
	// FIN TO DO

	public void process(WatchedEvent event) { 
		// TO DO 
            List<String> miembros;
			try {
				miembros = zk.getChildren(rootMembers, this);
				quorumManager.setnServers(miembros.size());
	            if (event != null && event.getPath().equals(rootMembers) == true) {
	                nMembers = nMembers + 1;
	            }
	            actualizarBBDD(event, miembros);
			} catch (KeeperException | InterruptedException e) {
				e.printStackTrace();
			}
		// FIN TO DO
	}
	
	// TO DO
	private void guardarDB(List<String> miembros) throws KeeperException, InterruptedException {
		clientDB.saveDBFile(ruta);
        zk.create(data, ConvertsByte.StringToByte(ruta), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        quorumManager.dbActualizada();
    }
    
    private boolean fijaLider(List<String> miembros) {
    	miembros.sort(null);
    	if (miembros != null && memberID.equals(miembros.get(0))) {
    		return true;
    	} else {
    		return false;
    	}
    }
	// FIN TO DO
}

package es.upm.dit.cnvr_fcon.bank2022.common;

import java.util.List;

import org.apache.zookeeper.ZooKeeper;

import es.upm.dit.cnvr_fcon.bank2022.bank.MainBank;


/**
 * Support for managing the number of process in a group.
 * Its aim is to support the faults tolerance.
 * @author aalonso
 *
 */
public class QuorumManager {

	private java.util.logging.Logger LOGGER = MainBank.LOGGER;
	private int quorum;	
	private ZooKeeper zk;
	private String rootMembers;
	
	/**
	 * The constructor
	 * @param zk the handler for interacting with a ZooKeeper ensemble
	 * @param quorum the quorum number. The processes that has been created
	 * and joined to the group
	 * @param rootMembers root of the znode for managing the groip
	 */
	public QuorumManager(ZooKeeper zk, int quorum, String rootMembers) {
		this.rootMembers = rootMembers;
		this.quorum      = quorum;
		this.zk          = zk; 
	}


	/**
	 * Check whether there is a quori,
	 * @return whether there is a quorum
	 */
	public boolean isQuorum() {
		Boolean isQuorum = false;
		
		try {
			List<String> list = zk.getChildren(rootMembers,  true); 
			isQuorum = list.size() == quorum;
			
		} catch (Exception e) {
			LOGGER.severe("Unexpected Exception process Watcher MEMBER");
		}
		return isQuorum;
	}

}

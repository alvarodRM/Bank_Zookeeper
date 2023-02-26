package es.upm.dit.cnvr_fcon.bank2022.common;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;

import es.upm.dit.cnvr_fcon.bank2022.bank.MainBank;

/**
 * Create a zNode
 * @author aalonso
 *
 */
public class CreateNode {

	private java.util.logging.Logger LOGGER = MainBank.LOGGER;

	private Stat checkPath(ZooKeeper zk, String path, Watcher watcher) {
		Stat s = null;
		try {
			// Create a node with the name root (member or opns), if it is not created
			// Set a watcher
			s = zk.exists(path, null);
			if (s == null) {
				return null;
			}
			return s;
		} catch (KeeperException e) {
			System.out.println("The session with Zookeeper failes. Closing");
			return s;
		} catch (InterruptedException e) {
			System.out.println("InterruptedException raised");
			return s;
		}
	}

	/**
	 * Create a zNode
	 * @param zk the handler for interacting with a ZooKeeper ensemble
	 * @param parent the parent of the node to be created
	 * @param child the node to be created
	 * @param mode the mode of the node
	 * @param data the data of the node
	 * @param watcher the watcher of the node. Null if it is not requred
	 * @return the name of the node created
	 */
	public String createNode(ZooKeeper zk, String parent, String child,
			CreateMode mode, byte[] data, Watcher watcher) {

		String myID = null;

		Stat s;
		if (zk != null) {
			if (parent != "/") {
				s = checkPath(zk, parent, watcher);

				if (s==null) {
					LOGGER.severe("The parent node is not created");
					return null;
				}
			}

			try {
				myID = zk.create(parent + child, data, Ids.OPEN_ACL_UNSAFE, mode);
				s = zk.exists(parent + child, watcher);
				zk.getChildren(parent,  watcher, s);
			} catch (KeeperException e) {
				System.out.println("Keeper exception when instantiating queue: "
						+ e.toString());
			} catch (InterruptedException e) {
				System.out.println("Interrupted exception");
			}
		}

		return myID;
	}

}
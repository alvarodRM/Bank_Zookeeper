package es.upm.dit.cnvr_fcon.bank2022.tobedone;

import es.upm.dit.cnvr_fcon.bank2022.bank.MainBank;

/**
 * Support for managing the number of process in a group.
 * Its aim is to support the faults tolerance.
 * 
 * @authors Luis Alberto López Álvarez y Álvaro de Rojas Maraver
 */
public class QuorumManagerFT {

	private java.util.logging.Logger LOGGER = MainBank.LOGGER;
	private int quorum;
	private int nServers;
	private boolean initQuorum = true;
	private enum Status {noQuorumInit, quorumInit, noQuorum, Quorum};
	private Status status = Status.noQuorumInit;
	private boolean pendingTransferDB = false;

	public QuorumManagerFT(int quorum) {
		this.quorum   = quorum;
		this.nServers = 0;
	}

	public int getQuorum() {
		return quorum;
	}
	public void setQuorum(int quorum) {
		this.quorum = quorum;
	}
	public int getnServers() {
		return nServers;
	}
	
	// TO DO
	public boolean setStatus(int n_servidores) {
		if((nServers > n_servidores && n_servidores >= 0) && (Status.quorumInit == status || Status.Quorum == status)) {
			pendingTransferDB = true;
			status = Status.noQuorum;
			return true;
		}
		else if((nServers < n_servidores && n_servidores >= 0) && (n_servidores == quorum && status.equals(Status.noQuorum))) {
			status = Status.Quorum;
			return true;
		} 
		else if((nServers < n_servidores && n_servidores >= 0) && (n_servidores == quorum && status.equals(Status.noQuorumInit))){
			status = Status.quorumInit;
			return true;
		} else {
			return false;
		}
	}
	
	public void setnServers(int n_servidores) {
		switch (status) {
	        case quorumInit: {
	        	boolean result = setStatus(n_servidores);
	        	if (result != false) {
	        		System.out.println("setStatus -> Iniciando Quorum");
	        	}
	        	break;
	        }
	        case Quorum: {
	        	boolean result = setStatus(n_servidores);
	        	if (result != false) {
	        		System.out.println("setStatus -> Hay Quorum.  Quorum=ServidoresON=" + n_servidores);
	        	}
	        	break;
	        }
	        case noQuorumInit: {
	        	boolean result = setStatus(n_servidores);
	        	if (result == false) {
	        		System.out.println("setStatus -> Todavia No hay Quorum.  Quorum=" + quorum + " , ServidoresON=" + n_servidores);
	        		break;
	        	} else {
	        		System.out.println("setStatus -> Hay Quorum.  Quorum=ServidoresON=" + n_servidores);
	        		break;
	        	}
	        }
	        case noQuorum: {
	        	boolean result = setStatus(n_servidores);
	        	if (result == false) {
	        		System.out.println("setStatus -> Todavia No hay Quorum");
	        		break;
	        	} else {
	        		System.out.println("setStatus -> Hay Quorum.  Quorum=ServidoresON=" + n_servidores);
	        		break;
	        	}
	        }
	    }
	    nServers = n_servidores;
	}
	
    public void dbActualizada() {
        pendingTransferDB = false;
    }
    
    public boolean dbPendiente() {
        return pendingTransferDB;
    }
	// FIN TO DO

	public boolean isQuorum() {
		LOGGER.finer("Quorum: " + status);
		return (status == Status.quorumInit) || (status == Status.Quorum);
	}

	public boolean isInitQuorum() {
		return status == Status.quorumInit;
	}

	public String toString() {
		String s = "";

		s = s + "nServers: "   + this.nServers;
		s = s + " isQuorum: "  + this.isQuorum();
		s = s + " status: "    + this.status;
		s = s + " pendingTransferDB: " + this.pendingTransferDB; 
		return s;
	}
}

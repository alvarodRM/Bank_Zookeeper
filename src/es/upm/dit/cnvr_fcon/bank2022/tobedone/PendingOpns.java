package es.upm.dit.cnvr_fcon.bank2022.tobedone;

import es.upm.dit.cnvr_fcon.bank2022.bank.MainBank;

/**
 * It allows for waiting and notifying the required functions for 
 * ensured the synchronized invocation from the clients.
 * 
 * @authors Luis Alberto López Álvarez y Álvaro de Rojas Maraver
 */
public class PendingOpns {
    // TO DO
    private Integer mutex;
    private String idOperacion;
    
    public PendingOpns() {
        mutex = -1;
        idOperacion = null;
    }
    
    public void mutex (Integer mutex, Integer num) throws InterruptedException {
    	if(num==1) {
    		synchronized (mutex) {
                mutex.notify();
        	}
    	} else {
    		synchronized (mutex) {
    			mutex.wait();
            }
    	}
    }
    
    public boolean unlockOpn(String id_Operacion) throws InterruptedException {
        if (id_Operacion.equals(idOperacion) && id_Operacion != null) {
        	mutex(mutex, 1);
            return true;
        } else {
        	return false;
        }
    }
    
    public void putOpn(String id_Operacion) throws InterruptedException {
        idOperacion = id_Operacion;
        mutex(mutex, 2);
    }
    // FIN TO DO
}

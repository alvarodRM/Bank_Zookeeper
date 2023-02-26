package es.upm.dit.cnvr_fcon.bank2022.common;

import java.io.Serializable;

/**
 * The enumeration allows to describe the operation to be
 * executed. Allows for creating an object encapsulating 
 * an operation bank. Useful for transmitting and storing
 * it on a znode
 * @author aalonso
 *
 */
public enum OperationEnum implements Serializable{
	CREATE_CLIENT,
	READ_CLIENT,
	UPDATE_CLIENT,
	DELETE_CLIENT,
	CREATE_BANK
}

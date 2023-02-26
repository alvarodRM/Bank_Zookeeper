package es.upm.dit.cnvr_fcon.bank2022.common;

import java.io.Serializable;

/**
 * Information for clients in thee bank
 * @author aalonso
 *
 */
public class Client implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private int accountNumber;
	private String name;
	private int balance;
	
	/**
	 * Create a client 
	 *
	 * @param accountNumber Account number
	 * @param name          Name of the client
	 * @param balance       Balance of the client
	 */
	public Client (int accountNumber, String name, int balance) {
		this.accountNumber = accountNumber;
		this.name          = name;
		this.balance       = balance;
	}

	/**
	 * Get the account number of the client
	 * @return the account number
	 */
	public int getAccountNumber() {
		return accountNumber;
	}

	/**
	 * Set the account number of the client
	 * @param accountNumber the new account number
	 */
	public void setAccountNumber(int accountNumber) {
		this.accountNumber = accountNumber;
	}

	/**
	 * Get the name of the client
	 * @return the name
	 */	
	public String getName() {
		return name;
	}

	/**
	 * Set the name of the client
	 * @param name the name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get the balance of a client
	 * @return the balance
	 */
	public int getBalance() {
		return balance;
	}

	/**
	 * Set the balance of a client
	 * @param balance the balance
	 */
	public void setBalance(int balance) {
		this.balance = balance;
	}

	@Override
	public String toString() {
		return "[" + accountNumber + ", " + name + ", " + balance + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + accountNumber;
		result = prime * result + balance;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Client other = (Client) obj;
		if (accountNumber != other.accountNumber)
			return false;
		if (balance != other.balance)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}

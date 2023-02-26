package es.upm.dit.cnvr_fcon.bank2022.common;


import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;

import es.upm.dit.cnvr_fcon.bank2022.bank.MainBank;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

/**
 * Provides functions for converting from the operation bank, string, 
 * and integer to an array of bytes, and vice versa
 * @author aalonso
 *
 */
public class ConvertsByte {


	/**
	 * Convert from an bank operation to an array of bytes
	 * @param operation the operation
	 * @return the array of bytes
	 */
	public static byte[] OperationToByte (OperationBank operation) {

		byte [] data = null;

		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(operation);
			oos.flush();
			data = bos.toByteArray();
		} catch (Exception e) {
			MainBank.LOGGER.severe("Convert OperationToByte: Unexpected exception");
		}

		return data;
	}

	/**
	 * Convert an array of bytes to a bank operation
	 * @param data an array of bytes
	 * @return the bank operation 
	 */
	public static OperationBank  ByteToOperation (byte[] data) {

		OperationBank operation = null;

		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(data);
			ObjectInputStream ois = new ObjectInputStream(bis);
			operation = (OperationBank) ois.readObject();
		} catch (Exception e) {
			MainBank.LOGGER.severe("Convert ByteToOperation: Unexpected exception");
		}

		return operation;
	}

	/**
	 * Convert from an integer to  an array of bytes
	 * @param i the integer
	 * @return the array of bytes
	 */
	public static byte[] IntToByte (int i) {

		try {
			ByteBuffer b = ByteBuffer.allocate(4);

			b.putInt(i);
			return b.array();
		} catch (Exception e) {
			MainBank.LOGGER.severe("Convert OperationToByte: Unexpected exception");
			return null;
		}

	}

	/**
	 * Convert an array of bytes to an integer
	 * @param data an array of bytes
	 * @return the integer 
	 */
	public static int byteToInt (byte[] data) {
		int i = -1;
		try {
			ByteBuffer buffer = ByteBuffer.wrap(data);
			i = buffer.getInt();
			return i;
		} catch (Exception E) {
			MainBank.LOGGER.severe("ByteToInt: Unexpected exception");
			System.out.println(E.toString());
			return i;
		}
	}

	/**
	 * Convert from an string to an array of bytes
	 * @param s the string
	 * @return the array of bytes
	 */
	public static byte[] StringToByte (String s) {
		return s.getBytes();
	}

	/**
	 * Convert an array of bytes to a string
	 * @param data an array of bytes
	 * @return the string 
	 */
	public static String ByteToString (byte[] data) {		
		return new String(data);
	}


	public static void main(String[] args) {

		Client client = new Client(1, "Alonso", 1000);
		OperationBank operation = new OperationBank
				(OperationEnum.CREATE_CLIENT, client);

		byte[] data = ConvertsByte.OperationToByte(operation);
		OperationBank op1 = ConvertsByte.ByteToOperation(data);

		Client cl1 = op1.getClient();

		System.out.println(cl1.toString());

		int i = 13;

		byte[] b = IntToByte(i);
		i = byteToInt(b);
		System.out.println(i);
	}
}

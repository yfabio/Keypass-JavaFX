package com.tech.developer.crypto;


/**
 * 
 * The CryptoUtilCallback is used to notify an instance that needs to be encrypt or decrypt
 * 
 * @author yfabio
 *
 */
public interface CryptoUtilCallback {
	
	public String encryptValue(String password);
	public String  decryptValue(String password);
}

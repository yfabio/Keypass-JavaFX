package com.tech.developer.crypto;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * The CryptoUtil is used to encrypt the password by using a simetric key and the AES algorithm
 * @author yfabio
 *
 */
public class CryptoUtil {
	
	/**
	 * 
	 * The encrypAES encrypts the password 
	 * 
	 * @param keyBytes
	 * @param dataBytes
	 * @return returns an array of bytes that is encrypted
	 * @throws CryptoException
	 */
	public static byte[] encryptAES(byte[] keyBytes, byte[] dataBytes) throws CryptoException {
		return handleAES(keyBytes,dataBytes,Cipher.ENCRYPT_MODE);
	}
	
	
	/**
	 * 
	 * The decryptAES decrypts the password
	 * 
	 * @param keyBytes
	 * @param dataBytes
	 * @return returns an array of byte decrypted
	 * @throws CryptoException
	 */
	public static byte[] decryptAES(byte[] keyBytes, byte[] dataBytes) throws CryptoException {
		return handleAES(keyBytes,dataBytes,Cipher.DECRYPT_MODE);
	}

	
	/**
	 * 
	 * the handleAES starts the cipher which encrypts or decrypts the password
	 * 
	 * @param keyBytes
	 * @param dataBytes
	 * @param mode
	 * @return returns an encrypted password or decrypted
	 * @throws CryptoException
	 */
	private static byte[] handleAES(byte[] keyBytes, byte[] dataBytes, int mode) throws CryptoException {
	
		if(keyBytes == null || keyBytes.length != 16) {
			throw new CryptoException("The key was invalid");
		}
		
		if(dataBytes == null) {
			throw new CryptoException("There wasn't any data");
		}
		
		try {
			SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
			
			Cipher cipher = Cipher.getInstance("AES");
			
			cipher.init(mode, key);
			
			return cipher.doFinal(dataBytes);
		} catch (Exception e) {
			throw new CryptoException(e);
		}		
	}

}

package com.tech.developer.persistance;

public class PersistanceException extends Exception {

	private static final long serialVersionUID = 1L;

	public PersistanceException(String message, Throwable cause) {
		super(message, cause);		
	}

	public PersistanceException(String message) {
		super(message);		
	}

	public PersistanceException(Throwable cause) {
		super(cause);		
	}

	
}

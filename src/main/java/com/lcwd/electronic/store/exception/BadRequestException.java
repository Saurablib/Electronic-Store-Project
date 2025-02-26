package com.lcwd.electronic.store.exception;

public class BadRequestException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4693846490751454781L;

	public BadRequestException(String message) {
		super(message);
	}
	
	public BadRequestException() {
		super("Bad Request!!");
	}

}

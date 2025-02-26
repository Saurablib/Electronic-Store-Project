package com.lcwd.electronic.store.exception;

public class ResourceNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ResourceNotFoundException() {
		super("Resoure Not Found");
	}

	public ResourceNotFoundException(String message) {

		super(message);
	}

}

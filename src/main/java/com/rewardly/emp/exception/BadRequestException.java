package com.rewardly.emp.exception;

public class BadRequestException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6964181959670500615L;

	// 400
	private static final String ERROR_CODE = "BAD_REQUEST";

	public BadRequestException(String id) {
			super(
					String.format("Bad Request with ID: %s",id),
							ERROR_CODE, id);
		}

}

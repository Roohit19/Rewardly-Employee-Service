package com.rewardly.emp.exception;

public class InvalidEmployeeDataException extends BaseException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4535203263598197383L;
	private static final String ERROR_CODE="INVALID_EMPLOYEE_DATA";
	
	public InvalidEmployeeDataException(String message) {
		super(message,ERROR_CODE);
	}
//	public InvalidEmployeeDataException(String message,Object... args) {
//		super(message,ERROR_CODE,args);
//	}
	public InvalidEmployeeDataException(String message,Throwable cause) {
		super(message,ERROR_CODE,cause);
	}
	
}

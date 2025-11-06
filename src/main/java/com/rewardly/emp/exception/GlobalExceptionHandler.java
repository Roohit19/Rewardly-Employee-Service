package com.rewardly.emp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	/**
	 *
	 */
	@ExceptionHandler(EmployeeNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleEmployeeNotFoundException(EmployeeNotFoundException ex, WebRequest request){
		ErrorResponse errorResponse = ErrorResponse.builder()
		.status(HttpStatus.NOT_FOUND.value())
		.errorCode(ex.getErrorCode())
		.errorMessage(ex.getMessage())
		.path(getRequestPath(request))
		.build();
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		
	}
	private String getRequestPath(WebRequest request){
		return request.getDescription(false).replace("uri=", "");
	}

	
}

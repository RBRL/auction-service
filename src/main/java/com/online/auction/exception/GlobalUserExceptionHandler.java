package com.online.auction.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.online.auction.model.ErrorResponse;

@RestControllerAdvice
public class GlobalUserExceptionHandler {
	
	@ExceptionHandler(value=ProductNotFoundException.class)
	public ResponseEntity<ErrorResponse> noRuleFoundException(ProductNotFoundException ex){
		ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		
	}
	
	@ExceptionHandler(value=AuctionServiceException.class)
	public ResponseEntity<Object> handleCustomerNotFoundException(
			AuctionServiceException ex) {
		    Map<String, Object> body = new HashMap<>();
		    body.put("message", ex.getMessage());
		    return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
		}
	
	 @ExceptionHandler(value = Exception.class)
	    public ResponseEntity<Object> databaseConnectionFailsException(Exception ex) {
		 ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
	    }
}

package com.online.auction.exception;

public class ProductNotFoundException extends RuntimeException {
	public ProductNotFoundException(){
		super();
	}
	public ProductNotFoundException(String message){
		super(message);
	}
	public ProductNotFoundException(Throwable t){
		super(t.getMessage());
		
	}
	public ProductNotFoundException(Throwable t,String message){
		super(message,t);
	}
}


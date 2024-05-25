package com.online.auction.exception;

public class AuctionServiceException extends Exception {
	public AuctionServiceException(){
		super();
	}
	public AuctionServiceException(String message){
		super(message);
	}
	public AuctionServiceException(Throwable t){
		super(t.getMessage());
		
	}
	public AuctionServiceException(Throwable t,String message){
		super(message,t);
	}
}

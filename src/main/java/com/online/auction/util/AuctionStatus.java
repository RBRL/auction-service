package com.online.auction.util;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;


public enum AuctionStatus {
		INPROGRESS("Inprogress"),
	    ENDED("Ended"),
	    WON("Won"),
	    LOST("Lost") ;
	    
	   
	    private final String name;
		AuctionStatus(String name)
		{ this.name = name; }
	    
	    @JsonValue
	    public String getValue() {
	        return this.name;
	    }
	    
	    @JsonCreator
	    public static AuctionStatus getValue(final String value) {
	    	for(AuctionStatus item : values()){
	            if(item.name.equalsIgnoreCase(value)){
	                return item;
	            }
	        }
	        return null;
	    }
}

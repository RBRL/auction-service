package com.online.auction.util;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;


public enum ProductStatus {
		AVAILABLE("Available"),
	    SOLD("Sold"),
	    UNSOLD("Unsold"),
	    DEACTIVATED("Deactivated") ;
	    
	   
	    private final String name;
		ProductStatus(String name)
		{ this.name = name; }
	    
	    @JsonValue
	    public String getValue() {
	        return this.name;
	    }
	    
	    @JsonCreator
	    public static ProductStatus getValue(final String value) {
	    	for(ProductStatus item : values()){
	            if(item.name.equalsIgnoreCase(value)){
	                return item;
	            }
	        }
	        return null;
	    }
}

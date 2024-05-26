//package com.online.auction.model;
//
//import java.math.BigDecimal;
//import java.util.Date;
//
//import com.fasterxml.jackson.annotation.JsonFormat;
//import com.fasterxml.jackson.annotation.JsonInclude;
//import com.fasterxml.jackson.annotation.JsonProperty;
//import com.fasterxml.jackson.annotation.JsonValue;
//import com.online.auction.util.ProductStatus;
//
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Data
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//@JsonInclude(JsonInclude.Include.NON_NULL)
//public class Product {
//	private Long id;
//	private String name;
//	private String description;
//	private String category;
//	@Builder.Default
//	private BigDecimal askPrice=new BigDecimal("0");
//	@JsonFormat(pattern = "dd-MM-yyyy hh:mm:ss")
//	private Date startTime;
//	@JsonFormat(pattern = "dd-MM-yyyy hh:mm:ss")
//	private Date endTime;
//	private ProductStatus status;
//		
//	
//}

package com.online.auction.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.online.auction.util.AuctionStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "AUCTION")
public class Auction {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
    @JsonManagedReference
	@OneToOne(cascade = { 
      		CascadeType.MERGE,
 	    		CascadeType.PERSIST,
 	    		CascadeType.REMOVE
   })
	@JoinColumn(name = "product_id")
	private Product product;
//	private Long productId;
	private String buyerId;
	@Builder.Default
	BigDecimal bidPrice = new BigDecimal(0);
	@JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	private LocalDateTime endTime;
	@JsonProperty
	private AuctionStatus status;
	
}

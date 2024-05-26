package com.online.auction.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.online.auction.util.ProductStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Singular;

import lombok.Setter;
import lombok.Getter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PRODUCT")
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;
	private String name;
	private String category;
	private String sellerName;
//    @JsonManagedReference
//	@OneToOne(cascade = { 
//        		CascadeType.MERGE,
//   	    		CascadeType.PERSIST,
//   	    		CascadeType.REMOVE
//    })
//	@JoinColumn(name = "auction")
	@JsonBackReference
	@OneToOne(mappedBy = "product", cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE })
	private Auction auction;
	@Builder.Default
	BigDecimal askPrice = new BigDecimal(0);
	@JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	private LocalDateTime startTime;
	@JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	private LocalDateTime endTime;
	@JsonProperty
	private ProductStatus status;
	@Builder.Default
	@OneToMany( mappedBy = "product")
	private List<Bid> bids = new ArrayList<Bid>();

	public void addBid(Bid bid) {
		bids.add(bid);
		bid.setProduct(this);
	}

}

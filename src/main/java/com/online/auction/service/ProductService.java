package com.online.auction.service;

import java.util.List;

import com.online.auction.entity.Auction;
import com.online.auction.entity.Bid;
import com.online.auction.entity.Product;
import com.online.auction.exception.AuctionServiceException;


public interface ProductService {
	public Integer addProducts(List<Product> product) throws AuctionServiceException;

	public List<Product> viewProducts() throws AuctionServiceException;

	public Bid bidForProduct(Bid bid) throws AuctionServiceException;

	public Auction endAuction(Long productID) throws AuctionServiceException;

	public List<Auction> viewAuctions() throws AuctionServiceException;

}

package com.online.auction.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.online.auction.entity.Auction;
import com.online.auction.entity.Bid;
import com.online.auction.entity.Product;
import com.online.auction.exception.AuctionServiceException;
import com.online.auction.service.ProductService;
import com.online.auction.util.JWTUtil;


@RestController
@RequestMapping("/auction")
public class AuctionController {
	
	@Autowired
	JWTUtil jwtUtil;

	@Autowired
	ProductService productService;

	@GetMapping("/home")
	public String welcome() {
		return "Welcome to aution service";
	}

	@PostMapping("/product/add")
	public ResponseEntity<String> addProducts(@RequestBody List<Product> products) throws AuctionServiceException {
		Integer count=productService.addProducts(products);
		return ResponseEntity.ok().body("Added "+ count +" number of records");
	}
	
	@GetMapping("/product/all")
	public ResponseEntity<List<Product>> viewProducts() throws AuctionServiceException {
		return ResponseEntity.ok().body(productService.viewProducts());
	}
	
	@PostMapping("/bid")
	public ResponseEntity<Bid> bidProduct(@RequestBody Bid bid) throws AuctionServiceException {
		Bid newBid=productService.bidForProduct(bid);
		return ResponseEntity.ok().body(newBid);
	}
	
	@PostMapping("/end/{productId}")
	public ResponseEntity<Auction> endAuction(@PathVariable Long productId) throws AuctionServiceException {
		Auction winner=productService.endAuction(productId);
		return ResponseEntity.ok().body(winner);
	}
	
	@GetMapping("/auction/all")
	public ResponseEntity<List<Auction>> viewAuctions() throws AuctionServiceException {
		List<Auction> auctions=productService.viewAuctions();
		return ResponseEntity.ok().body(auctions);
	}
	

}

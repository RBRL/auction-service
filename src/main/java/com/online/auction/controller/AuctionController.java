package com.online.auction.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.online.auction.entity.Product;
import com.online.auction.exception.AuctionServiceException;
import com.online.auction.util.JWTUtil;


@RestController
@RequestMapping("/auction")
public class AuctionController {
	
	@Autowired
	JWTUtil jwtUtil;

	@GetMapping("/home")
	public String welcome() {
		return "Welcome to aution service";
	}
	
	@PostMapping("/register")
	public ResponseEntity<String> registerUser(@RequestBody Product product) throws AuctionServiceException {
		return ResponseEntity.ok().body("Hello from Auction Service");
	}

}

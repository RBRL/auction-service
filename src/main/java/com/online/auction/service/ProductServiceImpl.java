package com.online.auction.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.data.domain.Sort;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.online.auction.entity.Auction;
import com.online.auction.entity.Bid;
import com.online.auction.entity.Product;
import com.online.auction.exception.AuctionServiceException;
import com.online.auction.exception.ProductNotFoundException;
import com.online.auction.repository.AuctionRepository;
import com.online.auction.repository.BidRepository;
import com.online.auction.repository.ProductRepository;
import com.online.auction.util.AuctionStatus;
import com.online.auction.util.ProductStatus;

import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Sort.Order;
@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

	@Autowired
	ProductRepository productRepository;

	@Autowired
	AuctionRepository auctionRepository;

	@Autowired
	BidRepository bidRepository;

	List<Product> products;

	@Override
	public Integer addProducts(List<Product> products) throws AuctionServiceException {
		log.info("Auction Service add products");
		List<Product> list = null;
		try {
			list = productRepository.saveAll(products);
			for (Product p : products) {
				Auction auction = Auction.builder().build();
				auction.setProduct(p);
				auction.setStatus(AuctionStatus.INPROGRESS);
				auction.setEndTime(p.getEndTime());
				auctionRepository.save(auction);
			}

		} catch (Exception e) {
			log.error("Auction Service error "+e.getMessage());
			throw new AuctionServiceException(e.getMessage());
		}
		log.info("Auction Service successfully added products");
		return list.size();

	}

	@Override
	public List<Product> viewProducts() throws AuctionServiceException {
		List<Product> list = null;
		try {
			list = productRepository.findAll();
			return list;
		} catch (Exception e) {
			log.error("Error while viewing products"+e.getMessage());
			throw new AuctionServiceException(e.getMessage());
		}
	}

	@Override
	public Bid bidForProduct(Bid bid) throws AuctionServiceException {
		Bid bidSaved = null;
		try {

			// find product ID and map the owing side of ManyToOne mapping
			// to fetch value for Foreign key
			log.info("Checking is product exists and is avaiable");
			Product prod = productRepository.findById(bid.getProdId())
					.orElseThrow(() -> new AuctionServiceException("Product not found"));

			// check product is AVAIABLE
			if (prod.getStatus().equals(ProductStatus.AVAILABLE)) {

				if(prod.getAskPrice().compareTo(bid.getBiddingPrice()) >0) {
					throw new AuctionServiceException("Bidding price cannot be less than Ask price of product");
				}
				// find if bid exists for user
				log.info("Checking previous bid exists for user");
				Optional<Bid> existingBid = bidRepository.findByProductIdAndBuyerId(bid.getProdId(), bid.getBuyerId());
						
				if (existingBid.isPresent()) {
					Bid oldBid=existingBid.get();
					if (oldBid.getStatus().equals(AuctionStatus.INPROGRESS)) {
						log.info("Previous bid exists for user only update bdding price");
						bid = mapOldToNew(oldBid, bid);
					}
				}
				bid.setStatus(AuctionStatus.INPROGRESS);
				bid.setProduct(prod);
				bid.setBidTime(getCurretDate());
				bidSaved = bidRepository.save(bid);

				// save product with new bid details
				prod.addBid(bidSaved);
				productRepository.save(prod);
			} else {
				log.error("Product is not auctioned");
				throw new AuctionServiceException("Product is not Avaialable");
			}

		} catch (Exception e) {
			log.error("Product is not auctioned",e.getMessage());
			throw new AuctionServiceException(e.getMessage());
		}
		return bidSaved;
	}

	private Bid mapOldToNew(Bid oldBid, Bid bid) {
		oldBid.setBiddingPrice(bid.getBiddingPrice());
		bid = oldBid;
		return bid;
	}

	@Override
	public Auction endAuction(Long prodId) throws AuctionServiceException {
		Bid highestBid = null;
		Auction auct;
		try {
			auct = auctionRepository.findByProductId(prodId)
					.orElseThrow(() -> new AuctionServiceException("Auction not found"));

			//only in progress auction can be ended
			log.info("Checking if auction for the product exists and is in progress");
			if (auct.getStatus().equals(AuctionStatus.INPROGRESS)) {
				//fetch bids by bidding price and earliest bid if price is same
				log.info("Fetching bids in sorted by price,time");
				List<Bid> bids=getOrderByPriceTime(auct.getProduct().getId());
				
				if(null!=bids && bids.size()>0) {
					highestBid=bids.stream().findFirst().get();
				}else {
					throw new AuctionServiceException("No Bids for this product with id " + auct.getProduct().getId());
				}
						
			} else {
				throw new AuctionServiceException("Auction Ended for this product" + auct.getProduct().getId());
			}

			// update product to status sold and end time
			log.info("Update product to sold status and auction to ended status");
			updateProduct(auct);

			//mark auction as ended
			auct.setBidPrice(highestBid.getBiddingPrice());
			auct.setBuyerId(highestBid.getBuyerId());
			auct.setEndTime(getCurretDate());
			auct.setStatus(AuctionStatus.ENDED);
			return auctionRepository.save(auct);
		} catch (AuctionServiceException e) {
			log.error("Error while ending an auction "+e.getMessage());
			throw new AuctionServiceException(e.getMessage());
		}
	}

	private void updateProduct(Auction auct) throws ProductNotFoundException {
		Product product = productRepository.findById(auct.getProduct().getId())
				.orElseThrow(() -> new ProductNotFoundException("Product not found"));
		product.setStatus(ProductStatus.SOLD);
		product.setEndTime(getCurretDate());
		productRepository.save(product);
	}

	private LocalDateTime getCurretDate() {
		LocalDateTime datetime = LocalDateTime.now(ZoneId.systemDefault());
		DateTimeFormatter formatters1 = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		String text1 = datetime.format(formatters1);
		LocalDateTime parsedDate = LocalDateTime.parse(text1, formatters1);

		log.info("dateTime--: " + datetime);
		log.info("Text format--- " + text1);
		log.info("parsedDate:-- " + parsedDate.format(formatters1));
		return parsedDate;
	}

	@Override
	public List<Auction> viewAuctions() throws AuctionServiceException {
		try {
			return auctionRepository.findAll();
		} catch (Exception e) {
			throw new AuctionServiceException(e.getMessage());
		}
	}

	public List<Bid> getOrderByPriceTime(Long long1) throws AuctionServiceException {
		List<Bid> bids=null;
		try {
		List<Order> orders = new ArrayList<Order>();

		Order order1 = new Order(Sort.Direction.DESC, "biddingPrice");
		orders.add(order1);

		Order order2 = new Order(Sort.Direction.ASC, "bidTime");
		orders.add(order2);

		 bids = bidRepository.findByProductId(long1,Sort.by(orders));
		}catch(Exception ex) {
			log.error("Error cannot sort the user bid records "+ex.getMessage());
			throw new  AuctionServiceException(ex.getMessage());
		}
		return bids;
	}
}

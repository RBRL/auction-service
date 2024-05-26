package com.online.auction.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.online.auction.entity.Bid;
@Repository
public interface BidRepository extends JpaRepository<Bid, Long> {
	Optional<Bid> findTopByOrderByBiddingPriceDesc();
	Optional<Bid> findByProductIdAndBuyerId(Long long1, String prodId);
	List<Bid> findByProductId(Long productId, Sort by);

}

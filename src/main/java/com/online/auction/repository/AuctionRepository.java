package com.online.auction.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.online.auction.entity.Auction;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, Long> {

	Optional<Auction> findByProductId(Long prodId);

}

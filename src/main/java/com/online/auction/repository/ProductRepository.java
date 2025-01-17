package com.online.auction.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.online.auction.entity.Product;
@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{

}

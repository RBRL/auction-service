package com.online.auction;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.online.auction.entity.Auction;
import com.online.auction.entity.Bid;
import com.online.auction.entity.Product;
import com.online.auction.exception.AuctionServiceException;
import com.online.auction.repository.AuctionRepository;
import com.online.auction.repository.BidRepository;
import com.online.auction.repository.ProductRepository;
import com.online.auction.util.AuctionStatus;
import com.online.auction.util.JWTUtil;
import com.online.auction.util.ProductStatus;
@SpringBootTest(classes=AuctionServiceApplication.class)
@AutoConfigureMockMvc
public class AuctionServiceTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	JWTUtil jwtUtil;

	
	@MockBean
	ProductRepository productRepository;
	
	@MockBean
	AuctionRepository auctionRepository;
	
	@MockBean
	BidRepository bidRepository;

	
	@Test
	public void addProductsTest() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		Product product1 = Product.builder().name("iPhone").category("Electronics").sellerName("seller1")
				.askPrice(new BigDecimal("300.00"))
				.status(ProductStatus.AVAILABLE).build();
		Product product2 = Product.builder().name("iPhone").category("Electronics").sellerName("seller1")
				.askPrice(new BigDecimal("300.00"))
				.status(ProductStatus.AVAILABLE).build();
		
		List<Product> list= new ArrayList<>();
		list.add(product1);
		list.add(product2);
		
		    ArrayList<String> roles = new ArrayList();
	        roles.add("ROLE_SELLER");
	        String token = jwtUtil.generateToken("seller1",roles);
	        assertNotNull(token);
		
		mvc.perform(post("/auction/product/add").contentType("application/json").header("Authorization", "Bearer "+token)
				.content(objectMapper.writeValueAsString(list))).andExpect(status().isOk());

	}


	
	@Test
	public void viewAllProductsTest() throws Exception {
		Product product1 = Product.builder().name("iPhone").category("Electronics").sellerName("seller1")
				.askPrice(new BigDecimal("300.00"))
				.status(ProductStatus.AVAILABLE).build();
		Product product2 = Product.builder().name("iPhone").category("Electronics").sellerName("seller1")
				.askPrice(new BigDecimal("300.00"))
				.status(ProductStatus.AVAILABLE).build();
		
		List<Product> list= new ArrayList<>();
		list.add(product1);
		list.add(product2);
		    ArrayList<String> roles = new ArrayList();
	        roles.add("ROLE_SELLER");
	        String token = jwtUtil.generateToken("seller1",roles);
	        assertNotNull(token);
	        
	        when(productRepository.findAll()).thenReturn((list));
		
	        MvcResult mvcResult= mvc.perform(MockMvcRequestBuilders.get("/auction/product/all").contentType("application/json").header("Authorization", "Bearer "+token))
		 .andExpect(status().isOk()).andReturn();

	}
	
	@Test
		public void bidForProductTest() throws Exception {
			ObjectMapper objectMapper = new ObjectMapper();
			Bid bid = Bid.builder().biddingPrice(new BigDecimal("400.00")).buyerId("buyer1").prodId(1l)
					.status(AuctionStatus.INPROGRESS).build();
			Product product = Product.builder().id(1l).name("iPhone").category("Electronics").sellerName("seller1")
					.askPrice(new BigDecimal("300.00"))
					.status(ProductStatus.AVAILABLE).build();
			
			    ArrayList<String> roles = new ArrayList();
		        roles.add("ROLE_BUYER");
		        String token = jwtUtil.generateToken("buyer1",roles);
		        assertNotNull(token);
			
		        
		    when(productRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(product));
		    when(bidRepository.save(Mockito.any(Bid.class))).thenReturn(bid);
		        
			mvc.perform(post("/auction/bid").contentType("application/json").header("Authorization", "Bearer "+token)
					.content(objectMapper.writeValueAsString(bid))).andExpect(status().isOk());

		}
	
	
	@Test
	public void bidForProductNoProductTest() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		Bid bid = Bid.builder().biddingPrice(new BigDecimal("400.00")).buyerId("buyer1").prodId(1l)
				.status(AuctionStatus.INPROGRESS).build();
		Product product = Product.builder().id(1l).name("iPhone").category("Electronics").sellerName("seller1")
				.askPrice(new BigDecimal("300.00"))
				.status(ProductStatus.AVAILABLE).build();
		
		    ArrayList<String> roles = new ArrayList();
	        roles.add("ROLE_BUYER");
	        String token = jwtUtil.generateToken("buyer1",roles);
	        assertNotNull(token);
		
	        
	        
	        MvcResult   mvcResult =	mvc.perform(post("/auction/bid").contentType("application/json").header("Authorization", "Bearer "+token)
				.content(objectMapper.writeValueAsString(bid))).andExpect(status().isBadRequest()).andReturn();
		
		Optional<AuctionServiceException> someException = Optional.ofNullable((AuctionServiceException) mvcResult.getResolvedException());
        someException.ifPresent( (se) -> assertThat(se, is(notNullValue())));
        someException.ifPresent( (se) -> assertThat(se, is(instanceOf(AuctionServiceException.class))));
        assertEquals(someException.get().getMessage(),"Product not found");

	}
	
	@Test
	public void bidForProductInvalidBiddingPriceTest() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		Bid bid = Bid.builder().biddingPrice(new BigDecimal("100.00")).buyerId("buyer1").prodId(1l)
				.status(AuctionStatus.INPROGRESS).build();
		Product product = Product.builder().id(1l).name("iPhone").category("Electronics").sellerName("seller1")
				.askPrice(new BigDecimal("300.00"))
				.status(ProductStatus.AVAILABLE).build();
		
		    ArrayList<String> roles = new ArrayList();
	        roles.add("ROLE_BUYER");
	        String token = jwtUtil.generateToken("buyer1",roles);
	        assertNotNull(token);
		
	        
	    when(productRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(product));
	    when(bidRepository.save(Mockito.any(Bid.class))).thenReturn(bid);
	        
	    MvcResult   mvcResult =	mvc.perform(post("/auction/bid").contentType("application/json").header("Authorization", "Bearer "+token)
				.content(objectMapper.writeValueAsString(bid))).andExpect(status().isBadRequest()).andReturn();
		
		Optional<AuctionServiceException> someException = Optional.ofNullable((AuctionServiceException) mvcResult.getResolvedException());
        someException.ifPresent( (se) -> assertThat(se, is(notNullValue())));
        someException.ifPresent( (se) -> assertThat(se, is(instanceOf(AuctionServiceException.class))));
        assertEquals(someException.get().getMessage(),"Bidding price cannot be less than Ask price of product");

	}
	
	@Test
	public void endAuctionForNoAuctionTest() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		Bid bid1 = Bid.builder().id(1l).biddingPrice(new BigDecimal("400.00")).buyerId("buyer1").prodId(1l)
				.status(AuctionStatus.INPROGRESS).build();
		Bid bid2 = Bid.builder().id(2l).biddingPrice(new BigDecimal("400.00")).buyerId("buyer2").prodId(1l)
				.status(AuctionStatus.INPROGRESS).build();
		
	    ArrayList<Bid> bids = new ArrayList();
	    bids.add(bid1);
	    bids.add(bid2);
	    
		Product product = Product.builder().id(1l).name("iPhone").category("Electronics").sellerName("seller1")
				.askPrice(new BigDecimal("300.00"))
				.status(ProductStatus.AVAILABLE).build();
		
		
		    ArrayList<String> roles = new ArrayList();
	        roles.add("ROLE_SELLER");
	        String token = jwtUtil.generateToken("seller1",roles);
	        assertNotNull(token);
		
	        
	     MvcResult   mvcResult	=mvc.perform(post("/auction/end/1").contentType("application/json").header("Authorization", "Bearer "+token)
				.content(objectMapper.writeValueAsString(bid1))).andExpect(status().isBadRequest()).andReturn();
		

		Optional<AuctionServiceException> someException = Optional.ofNullable((AuctionServiceException) mvcResult.getResolvedException());
        someException.ifPresent( (se) -> assertThat(se, is(notNullValue())));
        someException.ifPresent( (se) -> assertThat(se, is(instanceOf(AuctionServiceException.class))));
        assertEquals(someException.get().getMessage(),"Auction not found");

	}

	@Test
	public void endAuctionTest() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		Bid bid1 = Bid.builder().id(1l).biddingPrice(new BigDecimal("400.00")).buyerId("buyer1").prodId(1l)
				.status(AuctionStatus.INPROGRESS).build();
		Bid bid2 = Bid.builder().id(2l).biddingPrice(new BigDecimal("400.00")).buyerId("buyer2").prodId(1l)
				.status(AuctionStatus.INPROGRESS).build();
		
	    ArrayList<Bid> bids = new ArrayList();
	    bids.add(bid1);
	    bids.add(bid2);
	    
		Product product = Product.builder().id(1l).name("iPhone").category("Electronics").sellerName("seller1")
				.askPrice(new BigDecimal("300.00"))
				.status(ProductStatus.AVAILABLE).build();
		
		Auction auction = Auction.builder().product(product).id(1l).bidPrice(null).buyerId(null).status(AuctionStatus.INPROGRESS).build();
		
		    ArrayList<String> roles = new ArrayList();
	        roles.add("ROLE_SELLER");
	        String token = jwtUtil.generateToken("seller1",roles);
	        assertNotNull(token);
		
	    when(productRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(product));
	    when(auctionRepository.findByProductId(Mockito.anyLong())).thenReturn(Optional.of(auction));
	    when(auctionRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(auction));
	    when(bidRepository.save(Mockito.any(Bid.class))).thenReturn(bid1);
	    when(auctionRepository.save(Mockito.any(Auction.class))).thenReturn(auction);
        when(bidRepository.findByProductId(Mockito.anyLong(),Mockito.any(Sort.class))).thenReturn(bids);
        
	        
		mvc.perform(post("/auction/end/1").contentType("application/json").header("Authorization", "Bearer "+token)
				.content(objectMapper.writeValueAsString(""))).andExpect(status().isOk());

	}
	
	@Test
	public void endAuctionNoBidsForProductTest() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		Product product = Product.builder().id(1l).name("iPhone").category("Electronics").sellerName("seller1")
				.askPrice(new BigDecimal("300.00"))
				.status(ProductStatus.AVAILABLE).build();
		Auction auction = Auction.builder().product(product).id(1l).bidPrice(null).buyerId(null).status(AuctionStatus.INPROGRESS).build();
		
		    ArrayList<String> roles = new ArrayList();
	        roles.add("ROLE_SELLER");
	        String token = jwtUtil.generateToken("seller1",roles);
	        assertNotNull(token);
		
	    when(auctionRepository.findByProductId(Mockito.anyLong())).thenReturn(Optional.of(auction));
	    when(auctionRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(auction));
        
	        
	    MvcResult  mvcResult=mvc.perform(post("/auction/end/1").contentType("application/json").header("Authorization", "Bearer "+token)
				.content(objectMapper.writeValueAsString(""))).andExpect(status().isBadRequest()).andReturn();
		
		Optional<AuctionServiceException> someException = Optional.ofNullable((AuctionServiceException) mvcResult.getResolvedException());
        someException.ifPresent( (se) -> assertThat(se, is(notNullValue())));
        someException.ifPresent( (se) -> assertThat(se, is(instanceOf(AuctionServiceException.class))));
        assertEquals(someException.get().getMessage(),"No Bids for this product with id "+1l);

	}

	
	private LocalDateTime getCurretDate() {
		LocalDateTime datetime = LocalDateTime.now(ZoneId.systemDefault());
		DateTimeFormatter formatters1 = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		String text1 = datetime.format(formatters1);
		LocalDateTime parsedDate = LocalDateTime.parse(text1, formatters1);
		return parsedDate;
	}

}

package com.online.auction.util;


import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JWTUtil {
	 
		@Value("${json.web.token.secret}")
	    public String secret;


	    public String extractUsername(String token) {
	        return extractClaim(token, Claims::getSubject);
	    }

	    public Date extractExpiration(String token) {
	        return extractClaim(token, Claims::getExpiration);
	    }

	    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
	        final Claims claims = extractAllClaims(token);
	        return claimsResolver.apply(claims);
	    }

	    private Claims extractAllClaims(String token) {
	        return Jwts
	                .parserBuilder()
	                .setSigningKey(getSignKey())
	                .build()
	                .parseClaimsJws(token)
	                .getBody();
	    }

	    private Boolean isTokenExpired(String token) {
	        return extractExpiration(token).before(new Date());
	    }

	    public Boolean validateToken(String token) {
	        final String username = extractUsername(token);
	        return (!isTokenExpired(token));
	    }

	    
	    public String generateToken(String userName,ArrayList<String> authorities){
	        Map<String,Object> claims=new HashMap<>();
	        //add roles in claims
	        claims.put("roles", authorities);
	        return createToken(claims,userName);
	    }
	    
	    private String createToken(Map<String, Object> claims, String userName) {
	        return Jwts.builder()
	                .setClaims(claims)
	                .setSubject(userName)
	                .setIssuedAt(new Date(System.currentTimeMillis()))
	                .setExpiration(new Date(System.currentTimeMillis()+1000*60*30))
	                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
	    }
	    private Key getSignKey() {
	        byte[] keyBytes= Decoders.BASE64.decode(secret);
	        return Keys.hmacShaKeyFor(keyBytes);
	    }
}

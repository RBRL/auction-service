package com.online.auction.filter;

import java.io.IOException;
import java.util.*;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.online.auction.exception.AuctionServiceException;
import com.online.auction.util.JWTUtil;

@Component
public class JWTFilter extends OncePerRequestFilter {

	@Autowired
	private JWTUtil jwtUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		boolean db = false;
		String path = request.getRequestURI();
		if (path.contains("h2-console")) {
			db = true;
		}

		if (!db) {
			try {
				String authHeader = request.getHeader("Authorization");
				String token = null;
				String username = null;
				if (authHeader != null && authHeader.startsWith("Bearer ")) {
					token = authHeader.substring(7);
					username = jwtUtil.extractUsername(token);
				}
				if (ObjectUtils.isEmpty(username)) {
					throw new AuctionServiceException("Missing authorization header");
				}

				// REST call to AUTH service
				// avoid network call
				jwtUtil.validateToken(token);

			} catch (Exception e) {
				throw new RuntimeException("Unauthorized access to the application");
			}
		}

		filterChain.doFilter(request, response);
	}

}

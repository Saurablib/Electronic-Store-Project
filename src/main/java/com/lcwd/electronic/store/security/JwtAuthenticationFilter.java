package com.lcwd.electronic.store.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter  {
	
	Logger logger = LoggerFactory.getLogger(OncePerRequestFilter.class);
	
	@Autowired
	private JwtHelper jwtHelper;

	@Autowired
	private UserDetailsService userDetailsService;
	
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String requestHeader = request.getHeader("Authorization");
		
		System.out.println("print request header :"+requestHeader);
		logger.info("Request Headr: {} ",requestHeader);
		
		String userName=null;
		String token = null;
		
		if(requestHeader != null && requestHeader.startsWith("Bearer")) {
			
			token = requestHeader.substring(7);
			
			try {
				
				userName = this.jwtHelper.getUsernameFromToken(token);
			} catch (IllegalArgumentException e) {
				logger.info("IllegalArgumentException while fetching Username !!");
				e.printStackTrace();
			}catch (ExpiredJwtException e) {
				logger.info("Given Jwt Token has Expired !!");
				e.printStackTrace();
			}catch (MalformedJwtException e) {
				logger.info("MalformedJwtException Inavalid value !!");
				e.printStackTrace();
			}catch (Exception e) {
				logger.info("Invalid logger value !!");
				e.printStackTrace();
			}
			
		}else {
			logger.info("Invalid logger value In else Block !!");
		}
		
		
		if(userName != null && SecurityContextHolder.getContext().getAuthentication()== null) {
			
			UserDetails userByUsername = userDetailsService.loadUserByUsername(userName);
			Boolean validateToken = this.jwtHelper.validateToken(token, userByUsername);
			
			if(validateToken) {
				
				UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userByUsername, null,userByUsername.getAuthorities());
			    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
				
				
				
			}else {
				logger.info("Validation fails!!");
			}
			
		}
		
		filterChain.doFilter(request, response);
		
	}
	
	

}

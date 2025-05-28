package com.crazycoder.service;

import org.springframework.security.core.userdetails.UserDetails;

import com.crazycoder.model.User;

public interface JWTService {
	
	public String generateJWTToken(User user );
	public String extractUserName(String token);
	public Boolean validateToken(String token,UserDetails userDetails);
    
}

package com.crazycoder.service;

import com.crazycoder.model.User;

public interface JWTService {
	
	public String generateJWTToken(User user );

}

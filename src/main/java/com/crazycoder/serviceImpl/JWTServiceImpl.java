package com.crazycoder.serviceImpl;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import com.crazycoder.model.User;
import com.crazycoder.service.JWTService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoder;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;


@Service
public class JWTServiceImpl implements JWTService {
	
	private String secretKey1="";
	
	public JWTServiceImpl() {
		try {
		KeyGenerator keyGen=KeyGenerator.getInstance("HmacSHA256");
			SecretKey secretKey = keyGen.generateKey();
			secretKey1 = Base64.getEncoder().encodeToString(secretKey.getEncoded());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String generateJWTToken(User user) {
		
		Map<String,Object> claims=new HashMap<>();
		claims.put("role", user.getRole());
		claims.put("id", user.getId());
		claims.put("status", user.getAccountStatus().getIsActive());
		
		String token = Jwts.builder()
		.claims().add(claims)
		.subject(user.getEmail())
		.issuedAt(new Date(System.currentTimeMillis()))
		.expiration(new Date(System.currentTimeMillis() + 60 + 60 + 60))
		.and()
		.signWith(getKey())
		.compact();
		return token;
	}

	private Key getKey() {
	byte[] keyBytes = Decoders.BASE64.decode(secretKey1);
		return Keys.hmacShaKeyFor(keyBytes);
	}

}

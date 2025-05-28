package com.crazycoder.serviceImpl;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.crazycoder.model.User;
import com.crazycoder.service.JWTService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;


@Service
public class JWTServiceImpl implements JWTService {
	
	private String globally_secretKey="";
	
	public JWTServiceImpl() {
		try {
		KeyGenerator keyGen=KeyGenerator.getInstance("HmacSHA256");
			SecretKey secretKey = keyGen.generateKey();
			globally_secretKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
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
		.expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
		.and()
		.signWith(getKey())
		.compact();
		return token;
	}

	private Key getKey() {
	byte[] keyBytes = Decoders.BASE64.decode(globally_secretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	@Override
	public String extractUserName(String token) {
          Claims claims = extractAllClaims(token);
		return claims.getSubject();
	}

	private Claims extractAllClaims(String token) {
		Claims claims = Jwts.parser()
				          .verifyWith(decryptKey(globally_secretKey))
				                  .build().parseSignedClaims(token)
				                             .getPayload();
		return claims;
	}

	private SecretKey decryptKey(String secretKey) {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	@Override
	public Boolean validateToken(String token, UserDetails userDetails) {
		String username = extractUserName(token);
		Boolean isExpired = isTokenExpired(token);
		
		if(username.equalsIgnoreCase(userDetails.getUsername()) && !isExpired) {
			return true;
		}
		return false;
	}

	private Boolean isTokenExpired(String token) {
		Claims claims = extractAllClaims(token);
		Date expiredDate = claims.getExpiration();
		return expiredDate.before(new Date());
	}

}

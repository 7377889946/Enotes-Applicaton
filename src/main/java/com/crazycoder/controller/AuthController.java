package com.crazycoder.controller;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crazycoder.commonUtil.CommonUtil;
import com.crazycoder.dto.UserDto;
import com.crazycoder.dto.loginRessponse;
import com.crazycoder.service.UserService;

import jakarta.mail.MessagingException;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/")
	public ResponseEntity<?> registerUser(@RequestBody UserDto userDto) throws UnsupportedEncodingException, MessagingException{
		Boolean register = userService.register(userDto);
		if(register) {
			return CommonUtil.createBuildResponseMessage("Register Success", HttpStatus.CREATED);
		} else {
			return CommonUtil.createErrorResponseMessage("Register failed", HttpStatus.INTERNAL_SERVER_ERROR);
		}	
	}
	
	

	@PostMapping("/login")
	public ResponseEntity<?> loginUser(@RequestBody com.crazycoder.dto.loginRequest loginRequest){
	    loginRessponse Ressponse= userService.login(loginRequest);
	    
	    if (ObjectUtils.isEmpty(Ressponse)) {
	    	return CommonUtil.createErrorResponseMessage("Invalid Credentional", HttpStatus.BAD_REQUEST);
	    } else {
	    	return CommonUtil.createBuildResponse(Ressponse, HttpStatus.OK);
	    }

	}
}

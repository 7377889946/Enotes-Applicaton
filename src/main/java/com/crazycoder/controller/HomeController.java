package com.crazycoder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.crazycoder.commonUtil.CommonUtil;
import com.crazycoder.exception.ResourceNotFoundException;
import com.crazycoder.service.HomeService;

@RestController
@RequestMapping("/api/v1/home")
public class HomeController {
	
	@Autowired
	private HomeService homeService;
	
	@GetMapping("/verify")
	public ResponseEntity<?> verifyUser(@RequestParam Integer uid,@RequestParam String code) throws ResourceNotFoundException{
		
		
		if(homeService.verifyAccount(uid, code)) {
			return CommonUtil.createBuildResponseMessage("Account Verification Sucess", HttpStatus.OK);
		} else {
			return CommonUtil.createErrorResponseMessage("Invalid URL ", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}

}

package com.crazycoder.service;

import java.io.UnsupportedEncodingException;

import com.crazycoder.dto.UserDto;
import com.crazycoder.dto.loginRequest;
import com.crazycoder.dto.loginRessponse;

import jakarta.mail.MessagingException;


public interface UserService {
	public Boolean register(UserDto userDto) throws UnsupportedEncodingException, MessagingException;
	public loginRessponse login(loginRequest loginrequest);
}

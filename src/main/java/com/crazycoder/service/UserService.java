package com.crazycoder.service;

import java.io.UnsupportedEncodingException;

import com.crazycoder.dto.UserDto;

import jakarta.mail.MessagingException;


public interface UserService {
	public Boolean register(UserDto userDto) throws UnsupportedEncodingException, MessagingException;
}

package com.crazycoder.service;

import com.crazycoder.exception.ResourceNotFoundException;

public interface HomeService {
	
	public Boolean verifyAccount(Integer userid,String verficationcode) throws ResourceNotFoundException;

}

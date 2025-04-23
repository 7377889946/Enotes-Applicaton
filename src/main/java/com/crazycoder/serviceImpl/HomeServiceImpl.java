package com.crazycoder.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crazycoder.exception.ResourceNotFoundException;
import com.crazycoder.exception.SuccessException;
import com.crazycoder.model.AccountStatus;
import com.crazycoder.model.User;
import com.crazycoder.repository.UserRepository;
import com.crazycoder.service.HomeService;

@Service
public class HomeServiceImpl implements HomeService {
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public Boolean verifyAccount(Integer userid, String verficationcode) throws ResourceNotFoundException {
		
		User user=userRepository.findById(userid).orElseThrow(() -> new ResourceNotFoundException("Invalid User id"));
		
		
		if(user.getAccountStatus().getVerificationCode()==null) {
			throw new SuccessException("Account alerady Verified");
		}
		
		if(user.getAccountStatus().getVerificationCode().equals(verficationcode)) {
			AccountStatus acStatus=user.getAccountStatus();
			acStatus.setIsActive(true);
			acStatus.setVerificationCode(null);
			//user.setAccountStatus(acStatus);
			userRepository.save(user);
			return true;
			
		} else {
			return false;
		}
	    
		
	}

}

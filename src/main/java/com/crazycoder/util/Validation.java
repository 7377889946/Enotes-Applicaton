package com.crazycoder.util;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import com.crazycoder.Enum.TodoStatus;
import com.crazycoder.dto.CategoryDto;
import com.crazycoder.dto.ToDoDto;
import com.crazycoder.dto.ToDoDto.statusDto;
import com.crazycoder.dto.UserDto;
import com.crazycoder.exception.ExitCategoryException;
import com.crazycoder.exception.ResourceNotFoundException;
import com.crazycoder.exception.dtoValidationException;
import com.crazycoder.model.Role;
import com.crazycoder.repository.RoleRepository;
import com.crazycoder.repository.UserRepository;

@Component
public class Validation {
    
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	//category validation check
	public void categoryDtoValidation(CategoryDto categoryDto) throws dtoValidationException {
		 
		Map<String, Object> exceptionContainer=new LinkedHashMap<>();
		
		if(ObjectUtils.isEmpty(categoryDto)) {
			throw new IllegalArgumentException("Category Object/JSON should not be empty or null");
		} else {
			
			//Check name field validation
			if(ObjectUtils.isEmpty(categoryDto.getName())) {
				exceptionContainer.put("name", "name field can not be empty");
			} else {
				if(categoryDto.getName().length()<10) {
					exceptionContainer.put("name", "name length is less than 10");
				}
				if(categoryDto.getName().length()>100) {
					exceptionContainer.put("name", "name length is grater than 100");
				}
			}
			
			//Check description field validation
			
			if(ObjectUtils.isEmpty(categoryDto.getDescription())) {
				exceptionContainer.put("description", "Description can not be null");
			}
			
			//Check isActive field validation
			
			if(ObjectUtils.isEmpty(categoryDto.getIsActive())) {
				exceptionContainer.put("isActive", "isActive field can not be null");
			} else {
				if(categoryDto.getIsActive()!=Boolean.FALSE.booleanValue() && categoryDto.getIsActive()!=Boolean.TRUE.booleanValue()) {
					exceptionContainer.put("isActive", "Invalid isActive filed, Only accetptable is TRUE/FALSE");
				}
			}
		}

		
		if(!ObjectUtils.isEmpty(exceptionContainer)) {
			throw new dtoValidationException(exceptionContainer);
		}
	}
	
	
	//to-do validation check  
	public void todoValidation(ToDoDto dto) throws ResourceNotFoundException {
		
		statusDto dtos=dto.getStatus();
		
		Boolean statusFound=false;
		
		for(TodoStatus st:TodoStatus.values()) {
			if(st.getId().equals(dtos.getId())) {
				statusFound=true;
			}
		}
		
		if(!statusFound) {
			throw new ResourceNotFoundException("Invalid status id ! Please give a valid status id");
		}
	}
	
	
	public void userValidation(UserDto userDto) {
	    if (!StringUtils.hasText(userDto.getFirstName())) {
	        throw new IllegalArgumentException("First name is invalid");
	    }
	    if (!StringUtils.hasText(userDto.getLastName())) {
	        throw new IllegalArgumentException("Last name is invalid");
	    }
	    if (!StringUtils.hasText(userDto.getEmail()) || !userDto.getEmail().matches(constant.EMAIL_REGEX)) { // ✅ No Semicolon Here
	        throw new IllegalArgumentException("EmailId is invalid");
	    } else{
	        
	    	//Email exists check out
	    	Boolean isEmialExsits = userRepository.existsByEmail(userDto.getEmail());
	    	
	    	
	    	if(isEmialExsits) {
	    		throw new ExitCategoryException("Email id is exists in the database");
	    	}
	    }
	    if (!StringUtils.hasText(userDto.getMobNo()) || !userDto.getMobNo().matches(constant.MOBILE_REGEX)) {
	        throw new IllegalArgumentException("Mobile number is invalid");
	    }
	    
	    if(CollectionUtils.isEmpty(userDto.getRoles())) {
	    	throw new IllegalArgumentException("Role is invalid");
	    } else {
	    	 List<Integer> roles= roleRepository.findAll().stream().map(r -> r.getId()).toList();
	    	 
	    	List<Integer> invalidReqRolesIds= userDto.getRoles().stream().map(r->r.getId())
	    	 .filter(roleid -> roles.contains(roleid)).toList();
	    	
	    	if(CollectionUtils.isEmpty(invalidReqRolesIds)) {
	    		throw new IllegalArgumentException("Role is invalid" + invalidReqRolesIds);
	    	}
	    }
	}
}

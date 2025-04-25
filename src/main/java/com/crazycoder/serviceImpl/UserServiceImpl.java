package com.crazycoder.serviceImpl;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import com.crazycoder.dto.EmailRequest;
import com.crazycoder.dto.UserDto;
import com.crazycoder.dto.loginRequest;
import com.crazycoder.dto.loginRessponse;
import com.crazycoder.model.AccountStatus;
import com.crazycoder.model.Role;
import com.crazycoder.model.User;
import com.crazycoder.repository.RoleRepository;
import com.crazycoder.repository.UserRepository;
import com.crazycoder.security.CustomUserDetails;
import com.crazycoder.security.SecurityConfig;
import com.crazycoder.service.JWTService;
import com.crazycoder.service.UserService;
import com.crazycoder.util.Validation;

import jakarta.mail.MessagingException;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private Validation validation;
	
	@Autowired
	private JWTService jwtService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private EmailSendService emailSendService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Override
	public Boolean register(UserDto userDto,String apiurl) throws UnsupportedEncodingException, MessagingException {
		validation.userValidation(userDto);
		User user=modelMapper.map(userDto, User.class);
		setRole(userDto,user);
		
		AccountStatus accountStatus = AccountStatus.builder()
				.isActive(false)
				.verificationCode(UUID.randomUUID().toString())
				.build();
		
		user.setAccountStatus(accountStatus);
		user.setPassword(new SecurityConfig().bCryptPasswordEncoder().encode(user.getPassword()));
		User savedUser=userRepository.save(user);
		if(!ObjectUtils.isEmpty(savedUser)) {
			//send email
            emailSend(savedUser,apiurl);
			return true;
		} else {
			return false;		
		}
	}
	
	
	private void emailSend(User savedUser,String apiurl) throws UnsupportedEncodingException, MessagingException {
			
			String message = "Hi, <b>[[username]]</b>"
			        + "<br> Your account register successfully. <br>"
					+"<br> Click the below link verify your account <br>"
					+"<a href='[[url]]'>Click Here</a> <br>"
					+"Thanks, <br> Enotes.com";
			
			message=message.replace("[[username]]", savedUser.getFirstName());
			message=message.replace("[[url]]", apiurl+"/api/v1/home/verify?uid="+savedUser.getId()+"&&code="+savedUser.getAccountStatus().getVerificationCode());
			
			EmailRequest emailRequest = EmailRequest.builder()
					.to(savedUser.getEmail())
					.title("Account Creating Confirmation")
					.subject("Account created Succesfully")
					.message(message)
					.build();
			
			emailSendService.sendMail(emailRequest);
		}


	private void setRole(UserDto userDto, User user) {
		List<Integer> reqRoleId= userDto.getRole().stream().map(r -> r.getId()).toList();
		List<Role> roles= roleRepository.findAllById(reqRoleId);
		user.setRole(roles); 
	}
	
	
	@Override
	public loginRessponse login(loginRequest loginrequest) {
		
		org.springframework.security.core.Authentication authentication=
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginrequest.getEmail(), loginrequest.getPassword()));
		
		if(authentication.isAuthenticated()) {
			CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
			String token =jwtService.generateJWTToken(customUserDetails.getUser());
			
			loginRessponse Ressponse = loginRessponse.builder()
					                                 .user(modelMapper.map(customUserDetails.getUser(), UserDto.class))
					                                 .token(token)
					                                 .build();
			return Ressponse;
		}
		return null;
	}
}

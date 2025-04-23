package com.crazycoder.serviceImpl;

import java.io.UnsupportedEncodingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import com.crazycoder.dto.EmailRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;


@Component
public class EmailSendService {
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Value("${spring.mail.username}")
	private String mailFrom;
	
	
	public void sendMail(EmailRequest emailSend) throws MessagingException, UnsupportedEncodingException {
		MimeMessage message = mailSender.createMimeMessage();
		
		MimeMessageHelper messageHelper = new MimeMessageHelper(message);
		
		messageHelper.setFrom(mailFrom,emailSend.getTitle());
		messageHelper.setTo(emailSend.getTo());
	    messageHelper.setSubject(emailSend.getSubject());
	    messageHelper.setText(emailSend.getMessage(),true);
	    mailSender.send(message);
	}

}

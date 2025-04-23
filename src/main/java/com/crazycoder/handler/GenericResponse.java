package com.crazycoder.handler;


import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
public class GenericResponse {
	
	private HttpStatus statusCode;
	private String status;
	private String message;
	private Object data;
	
	public ResponseEntity<?> create(){
		Map<String, Object> storedData=new LinkedHashMap<>();
		storedData.put("status", status);
		storedData.put("message", message);
		
		if(!ObjectUtils.isEmpty(data)) {
	        storedData.put("data", data);
		}
		return new ResponseEntity<>(storedData,statusCode);
	}
	
}

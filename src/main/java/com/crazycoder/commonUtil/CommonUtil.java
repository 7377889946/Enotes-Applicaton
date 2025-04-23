package com.crazycoder.commonUtil;

import java.util.Collection;
import org.apache.commons.io.FilenameUtils;
import org.springframework.cglib.core.CollectionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;

import com.crazycoder.handler.GenericResponse;

public class CommonUtil {
	
	//if API getting success 
	public static ResponseEntity<?> createBuildResponse(Object data,HttpStatus status){
		

			  GenericResponse genericResponse=GenericResponse.builder()
	        		  .statusCode(status)
	        		  .status("Success")
	        		  .message("message")
	        		  .data(data)
	        		  .build();
          
          return genericResponse.create();
	}
	
	public static ResponseEntity<?> createBuildResponseMessage(String message,HttpStatus status){
        GenericResponse genericResponse=GenericResponse.builder()
      		  .statusCode(status)
      		  .status("Success")
      		  .message(message)
      		  .build();
        return genericResponse.create();
	}
	
	public static ResponseEntity<?> createErrorResponse(Object data,HttpStatus status){
        GenericResponse genericResponse=GenericResponse.builder()
      		  .statusCode(status)
      		  .status("Failed")
      		  .data(data)
      		  .message("Failed")
      		  .build();
        return genericResponse.create();
	}
	
	public static ResponseEntity<?> createErrorResponseMessage(String message,HttpStatus status){
        GenericResponse genericResponse=GenericResponse.builder()
      		  .statusCode(status)
      		  .status("Failed")
      		  .message(message)
      		  .build();
        return genericResponse.create();
	}

	public static String getContentType(String originalFileName) {
		String extension= FilenameUtils.getExtension(originalFileName);
		
		switch(extension) {
		
		case "pdf":
			 return "application/pdf";
		case "xlsx":
			 return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
		case "txt":
			 return "text/plain";
		 case "pptx":
		        return "application/vnd.openxmlformats-officedocument.presentationml.presentation";
		case  "png":
			return "image/png";
		case "jpeg":
			return "image/jpeg";
		default:
	        return "application/octet-stream";
			 
		}
	
	}
	
	
	
	 
	
}

package com.crazycoder.exception;

import java.util.LinkedHashMap;
import java.util.Map;

public class dtoValidationException extends Exception{
	
	private Map<String, Object> exceptionContainer=new LinkedHashMap<>();
	
	public dtoValidationException(Map<String, Object> exceptionContainer) {
		super("validation exception");
		this.exceptionContainer=exceptionContainer;
	}
	
	public Map<String, Object> getErrors() {
		return exceptionContainer;
	}
	

}

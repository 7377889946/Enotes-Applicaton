package com.crazycoder.exception;
import java.io.FileNotFoundException;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.crazycoder.commonUtil.CommonUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(NullPointerException.class)
	public ResponseEntity<?> handleNullPointerException(NullPointerException e){
		log.error("Global Exception handler :: HandleNullPointerException :- ",e.getMessage());
//      return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        return CommonUtil.createErrorResponseMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<?> handleResouceNotFoundException(ResourceNotFoundException e){
		log.error("Global Exception handler :: HandleResourceNotFoundException :- ",e.getMessage());
		return CommonUtil.createErrorResponseMessage(e.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> handleMethodArgumentNotFoundException(MethodArgumentNotValidException e){
		java.util.List<ObjectError> bindingError=e.getBindingResult().getAllErrors();
		Map<String, Object> errorContainer=new LinkedHashMap<>();
		bindingError.stream().forEach(error -> {
			String field = ((FieldError) error).getField();
			String msg = error.getDefaultMessage();
		    errorContainer.put(field, msg);
		});
		return new ResponseEntity<>(errorContainer,HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(dtoValidationException.class)
	public ResponseEntity<?> handleCategoryValidationException(dtoValidationException e){
		return CommonUtil.createErrorResponse(e.getErrors(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ExitCategoryException.class)
	public ResponseEntity<?> handleExitCategoryException(ExitCategoryException e){
		return CommonUtil.createErrorResponseMessage(e.getMessage(), HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException e){
		return CommonUtil.createErrorResponseMessage(e.getMessage(), HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<?> handleHttpMessageNotReadableException(IllegalArgumentException e){
		return CommonUtil.createErrorResponseMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(FileNotFoundException.class)
	public ResponseEntity<?> handleHttpMessageNotReadableException(FileNotFoundException e){
		return CommonUtil.createErrorResponseMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	
			
}

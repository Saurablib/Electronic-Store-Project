package com.lcwd.electronic.store.exception;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.lcwd.electronic.store.dtos.ApiResponseMessage;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiResponseMessage> resourceNotFoundException(ResourceNotFoundException ex){
		
		ApiResponseMessage apiResponseMessage = ApiResponseMessage.builder()
				.message(ex.getMessage())
				.succsess(false)
				.response(HttpStatus.NOT_FOUND)
				.build();
		return new ResponseEntity<ApiResponseMessage>(apiResponseMessage,HttpStatus.NOT_FOUND);	
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> handleMethodNotFoundException(MethodArgumentNotValidException ex){
		
		List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();
		
		HashMap<String, Object> response = new HashMap<String, Object>();
		allErrors.stream().forEach(objectError ->{
			
			String message = objectError.getDefaultMessage();
			String field = ((FieldError) objectError).getField();		
			response.put(field, message);
		});	
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.BAD_REQUEST);		
	}
	
	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<ApiResponseMessage> badRequestException(BadRequestException ex){
		
		ApiResponseMessage apiResponseMessage = ApiResponseMessage.builder()
				.message(ex.getMessage())
				.succsess(false)
				.response(HttpStatus.BAD_REQUEST)
				.build();
		return new ResponseEntity<ApiResponseMessage>(apiResponseMessage,HttpStatus.BAD_REQUEST);	
	}
	
	

}

package com.lcwd.electronic.store.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lcwd.electronic.store.dtos.ApiResponseMessage;
import com.lcwd.electronic.store.dtos.CreateOrderRequest;
import com.lcwd.electronic.store.dtos.OrderDto;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.service.OrderService;

@RestController
@RequestMapping("/orders")
public class OrderController {
	
	@Autowired
	private OrderService orderService;
	
	
	@PostMapping("/create")
	public ResponseEntity<ApiResponseMessage> createOrder(@RequestBody CreateOrderRequest orderDto){
		
		ApiResponseMessage response = ApiResponseMessage.builder()
				.succsess(true)
				.data(orderService.createOrder(orderDto))
				.message("Order Completed Successfully")
				.response(HttpStatus.CREATED).build();	
		return new ResponseEntity<ApiResponseMessage>(response,HttpStatus.CREATED);	
	}
	
	
	@DeleteMapping("/remove/{orderId}")
	public ResponseEntity<ApiResponseMessage> removeOrder(@PathVariable String orderId){
		
		orderService.removeOrder(orderId);
		ApiResponseMessage apiResponseMessage = ApiResponseMessage.builder()
				.message("Order Removed Successfully")
				.response(HttpStatus.OK).succsess(true)
				.build();
		return new ResponseEntity<ApiResponseMessage>(apiResponseMessage,HttpStatus.OK);
		
	}
	
	
	@GetMapping("/user/{userId}")
	public ResponseEntity<List<OrderDto>> getOrderOfUsers(@PathVariable Integer userId){
		
		List<OrderDto> orderOfUsers = orderService.getOrderOfUsers(userId);
		return new ResponseEntity<>(orderOfUsers,HttpStatus.OK);
		
	}
	
	@GetMapping
	public ResponseEntity<PageableResponse<OrderDto>> getAllOrders(
			@RequestParam(value = "pageNumber",defaultValue = "0",required = false) int  pageNumber,
			@RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
			@RequestParam(value = "sortBy",defaultValue = "orderDate",required = false) String  sortBy,
			@RequestParam(value = "sortDir",defaultValue = "desc",required = false) String sortDir){
		
		PageableResponse<OrderDto> allOrders = orderService.getAllOrders(pageSize, pageNumber, sortBy, sortDir);	
	    return new ResponseEntity<>(allOrders,HttpStatus.OK);	
	}
	

}

package com.lcwd.electronic.store.service;

import java.util.List;

import com.lcwd.electronic.store.dtos.CreateOrderRequest;
import com.lcwd.electronic.store.dtos.OrderDto;
import com.lcwd.electronic.store.dtos.PageableResponse;

public interface OrderService {
	
	//create order
	OrderDto createOrder(CreateOrderRequest orderDto);
	
	
	//remove user
	void removeOrder(String orderId);
	
	//getOrderByUser
	List<OrderDto> getOrderOfUsers(Integer userId);
	
	//getAllOrders
	PageableResponse<OrderDto> getAllOrders(int pageSize, int pageNumber, String SortBy, String SortDir);






	
	

}
 
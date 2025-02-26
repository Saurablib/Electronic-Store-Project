package com.lcwd.electronic.store.service;

import com.lcwd.electronic.store.dtos.AddItemToCartRequest;
import com.lcwd.electronic.store.dtos.CartDto;

public interface CartService {
	
	CartDto addItemtoCartDto(Integer userId,AddItemToCartRequest request);
	
	void removeItemFromCart(Integer userId, int cartItemId);
	
	void clearItemFromCart(Integer userId);
	
	CartDto getCartByUser(Integer userId);

}

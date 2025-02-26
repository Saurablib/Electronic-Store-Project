package com.lcwd.electronic.store.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lcwd.electronic.store.dtos.AddItemToCartRequest;
import com.lcwd.electronic.store.dtos.ApiResponseMessage;
import com.lcwd.electronic.store.service.CartService;

@RestController
@RequestMapping("/carts")
public class CartController {
	
	@Autowired
	CartService cartService;
	
	@PostMapping("/{userId}")
	public ResponseEntity<ApiResponseMessage> addItemToCart(
			@PathVariable Integer userId,
			@RequestBody AddItemToCartRequest addItemToCartRequest){
		
      ApiResponseMessage apiResponseMessage = ApiResponseMessage.builder()
		.succsess(true)
		.data(cartService.addItemtoCartDto(userId, addItemToCartRequest))
		.message("Item Added Successfully. !!")
		.response(HttpStatus.CREATED)
		.build();	 
		return new ResponseEntity<ApiResponseMessage>(apiResponseMessage, HttpStatus.CREATED);	
	}
	
	@DeleteMapping("/{userId}/items/{cartItemId}")
	public ResponseEntity<ApiResponseMessage> deleteItemFromCart(@PathVariable Integer userId,
			@PathVariable Integer cartItemId) {
		cartService.removeItemFromCart(userId, cartItemId);
		ApiResponseMessage apiResponseMessage = ApiResponseMessage.builder()
				.succsess(true)
				.message("items deleted successfully !!")
				.response(HttpStatus.OK)
				.build();
		return new ResponseEntity<ApiResponseMessage>(apiResponseMessage, HttpStatus.OK);

	}
	
	
	@DeleteMapping("/{userId}")
	public ResponseEntity<ApiResponseMessage> deleteItemFromCart(@PathVariable Integer userId) {
		cartService.clearItemFromCart(userId);
		ApiResponseMessage apiResponseMessage = ApiResponseMessage.builder()
				.succsess(true)
				.message("items cleared from cart !!")
				.response(HttpStatus.OK)
				.build();
		return new ResponseEntity<ApiResponseMessage>(apiResponseMessage, HttpStatus.OK);

	}
	
	
	@GetMapping("/{userId}")
	public ResponseEntity<ApiResponseMessage> getItemFromCart(@PathVariable Integer userId){
		
      ApiResponseMessage apiResponseMessage = ApiResponseMessage.builder()
		.succsess(true)
		.data(cartService.getCartByUser(userId))
		.response(HttpStatus.OK)
		.build();	 
		return new ResponseEntity<ApiResponseMessage>(apiResponseMessage, HttpStatus.OK);	
	}

}

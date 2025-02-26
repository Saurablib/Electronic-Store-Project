package com.lcwd.electronic.store.serviceImpl;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lcwd.electronic.store.dtos.AddItemToCartRequest;
import com.lcwd.electronic.store.dtos.CartDto;
import com.lcwd.electronic.store.entities.Cart;
import com.lcwd.electronic.store.entities.CartItem;
import com.lcwd.electronic.store.entities.Product;
import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.exception.BadRequestException;
import com.lcwd.electronic.store.exception.ResourceNotFoundException;
import com.lcwd.electronic.store.repositories.CartItemRepository;
import com.lcwd.electronic.store.repositories.CartRepository;
import com.lcwd.electronic.store.repositories.ProductRepository;
import com.lcwd.electronic.store.repositories.UserRepository;
import com.lcwd.electronic.store.service.CartService;

@Service
public class CartServiceImpl implements CartService {
	
	@Autowired
	ProductRepository productRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	CartRepository cartRepository;
	
	@Autowired
	CartItemRepository cartItemRepository;
	
	@Autowired
	ModelMapper mapper;

	
	@Override
	public CartDto addItemtoCartDto(Integer userId, AddItemToCartRequest request) {
	    String productId = request.getProductId();
	    int quantity = request.getQuantity();

	    if (quantity <= 0) {
	        throw new BadRequestException("Requested quantity is not valid. !!");
	    }

	    Product product = productRepository.findById(productId)
	        .orElseThrow(() -> new ResourceNotFoundException("Product Not Found With Given Id!!"));

	    User user = userRepository.findById(userId)
	        .orElseThrow(() -> new ResourceNotFoundException("User Not Found With Given Id!!"));

	    Cart cart;
	    try {
	        cart = cartRepository.findByUser(user)
	            .orElseThrow(() -> new ResourceNotFoundException("Cart Not Found!"));
	    } catch (Exception e) {
	        cart = new Cart();
	        cart.setCreatedAt(new Date());
	        cart.setCartId(UUID.randomUUID().toString());
	    }

	    List<CartItem> items = cart.getItems();
	    AtomicReference<Boolean> updated = new AtomicReference<>(false);

	    items.forEach(item -> {
	        if (item.getProduct().getProductId().equals(productId)) {
	            item.setQuantity(quantity);
	            item.setTotalPrice(quantity * product.getDiscountPrice());
	            updated.set(true); // Flag that we've updated an existing item
	        }
	    });


	    if (!updated.get()) {
	        CartItem cartItem = CartItem.builder()
	            .quantity(quantity)
	            .totalPrice(quantity * product.getDiscountPrice())
	            .cart(cart)
	            .product(product)
	            .build();
	        items.add(cartItem); 
	    }
	    cart.setItems(items);
	    
	    cart.setUser(user);
	    Cart updatedCart = cartRepository.save(cart);
	    return mapper.map(updatedCart, CartDto.class);
	}

	

	@Override
	public void removeItemFromCart(Integer userId, int cartItemId) {
		
		CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(() -> new ResourceNotFoundException("cartItem Not found!!"));		
		cartItemRepository.delete(cartItem);
	
	}

	@Override
	public void clearItemFromCart(Integer  userId) {
		
		 User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User Not Found With Given Id. !!"));	 
		 Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Cart Of given User not found"));		 
		 cart.getItems().clear();
		 cartRepository.save(cart);
	}


	@Override
	public CartDto getCartByUser(Integer userId) {
		
		 User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User Not Found With Given Id. !!"));	 
		 Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Cart Of given User not found"));		
		 return mapper.map(cart, CartDto.class);
	}

}

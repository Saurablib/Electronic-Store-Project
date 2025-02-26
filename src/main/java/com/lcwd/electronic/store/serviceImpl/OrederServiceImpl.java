package com.lcwd.electronic.store.serviceImpl;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.lcwd.electronic.store.dtos.CreateOrderRequest;
import com.lcwd.electronic.store.dtos.OrderDto;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.entities.Cart;
import com.lcwd.electronic.store.entities.CartItem;
import com.lcwd.electronic.store.entities.Order;
import com.lcwd.electronic.store.entities.OrderItem;
import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.exception.BadRequestException;
import com.lcwd.electronic.store.exception.ResourceNotFoundException;
import com.lcwd.electronic.store.repositories.CartRepository;
import com.lcwd.electronic.store.repositories.OrderRepository;
import com.lcwd.electronic.store.repositories.UserRepository;
import com.lcwd.electronic.store.service.OrderService;
import com.lcwd.electronic.store.util.Util;

@Service
public class OrederServiceImpl implements OrderService{
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	CartRepository cartRepository;
	
	@Autowired
	OrderRepository orderRepository;
	
	@Autowired
	ModelMapper modelMapper;
	
	
	@Override
	public OrderDto createOrder(CreateOrderRequest orderDto) {

	    Integer userId = orderDto.getUserId();
	    String cartId = orderDto.getCartId();
	    
	    System.out.println("print cart id---->" + cartId);

	    User user = userRepository.findById(userId).orElseThrow(() ->  new ResourceNotFoundException("User not found by given Id. !!"));

	    Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new ResourceNotFoundException("Cart not found by given Id. !!"));

	    List<CartItem> cartItems = cart.getItems();

	    System.out.println("print cart items -->" + cartItems);

	    if(cartItems.size() <= 0) {
	        throw new BadRequestException("Invalid number of items in cart");
	    }

	    Order order = Order.builder()
	            .billingAddress(orderDto.getBillingAddress())
	            .billingName(orderDto.getBillingName())
	            .billingPhone(orderDto.getBillingPhone())
	            .orderStatus(orderDto.getOrderStatus())
	            .orderDate(new Date())
	            .deliveredDate(orderDto.getDeliveredDate())
	            .orderId(UUID.randomUUID().toString())
	            .paymentStatus(orderDto.getPaymentStatus())
	            .user(user).build();

	    // Initialize orderItems list explicitly
	    AtomicReference<Integer> orderAmount = new AtomicReference<>(0);
	    List<OrderItem> orderItems = 
	     cartItems.stream().map(cartItem -> {
	        OrderItem orderItem = OrderItem.builder()
	                .quantity(cartItem.getQuantity())
	                .product(cartItem.getProduct())
	                .totalPrice(cartItem.getQuantity() * cartItem.getProduct().getDiscountPrice())
	                .order(order)
	                .build();
	        orderAmount.set((int) (orderAmount.get() + orderItem.getTotalPrice()));
	        //orderItems.add(orderItem); 
	        return orderItem;
	    }).collect(Collectors.toList());

	    order.setOrderItems(orderItems);  // Set the list of order items in the order
	    order.setOrderAmount(orderAmount.get());

	    // Clear the cart
	    cart.getItems().clear();
	    cartRepository.save(cart);

	    // Save the order
	    Order savedOrder = orderRepository.save(order);
	    return modelMapper.map(savedOrder, OrderDto.class);
	}


	@Override
	public void removeOrder(String orderId) {
		
		Order order = orderRepository.findById(orderId).orElseThrow(()-> new ResourceNotFoundException("Order not found by given Id. !!"));
		orderRepository.delete(order);
	}

	@Override
	public List<OrderDto> getOrderOfUsers(Integer userId) {
		
		User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException(" User Not found By Given Id"));	
		List<Order> orders = orderRepository.findByUser(user);	
		List<OrderDto> orderDtos = orders.stream().map(order -> modelMapper.map(order, OrderDto.class)).collect(Collectors.toList());
		return orderDtos;
	}

	@Override
	public PageableResponse<OrderDto> getAllOrders(int pageSize, int pageNumber, String sortBy, String sortDir) {
	
        Sort sort = (sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		Page<Order> page = orderRepository.findAll(pageable);	
		return Util.pageanbleResponse(page, OrderDto.class);
	}

}

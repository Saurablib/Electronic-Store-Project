package com.lcwd.electronic.store.dtos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class OrderDto {
	
	private String orderId;
	private String billingAddress;
	private Integer billingPhone;
	private String billingName;
	private Date deliveredDate;
	private Integer orderAmount ;
	private Date orderDate;
	private String orderStatus = "Pending";
	private String paymentStatus = "NotPaid";
	
	List<OrderItemDto> orderItems = new ArrayList<>();

}

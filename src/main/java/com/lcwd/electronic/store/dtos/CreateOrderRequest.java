package com.lcwd.electronic.store.dtos;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CreateOrderRequest {
	
	private Integer userId;
	private String cartId;
	private String billingAddress;
	private Integer billingPhone;
	private String billingName;
	private Date deliveredDate;
	private String orderStatus = "Pending";
	private String paymentStatus = "NotPaid";
	

}

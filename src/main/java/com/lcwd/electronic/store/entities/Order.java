package com.lcwd.electronic.store.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Getter
@Setter
@Table(name = "orders")
public class Order {

	@Id
	private String orderId;
	
	@Column(name = "billing_address",length = 100)
	private String billingAddress;
	
	@Column(name = "billing_phone")
	private Integer billingPhone;
	
	@Column(name = "billing_name")
	private String billingName;
	
	@Column(name = "delivered_date")
	private Date deliveredDate;
	
	@Column(name = "order_amount")
	private Integer orderAmount;
	
	@Column(name = "order_status")
	private String orderStatus;
	
	@Column(name = "order_date")
	private Date orderDate;
	
	@Column(name = "payment_status")
	private String paymentStatus;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	private User user;
	
	@OneToMany(mappedBy = "order", fetch = FetchType.EAGER,cascade = CascadeType.ALL)
	List<OrderItem> orderItems = new ArrayList<>();
	
}

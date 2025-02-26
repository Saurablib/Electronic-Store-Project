package com.lcwd.electronic.store.entities;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "products")
public class Product {
	
	@Id
	@Column(name = "product_id")
	private String productId;
	
	@Column(name = "product_title",length = 100)
	private String title;
	
	@Column(name = "product_desc", length = 1000)
	private String productDescription;
	
	@Column(name = "price")
	private double price;
	
	@Column(name = "dicount_price")
	private double discountPrice;
	
	@Column(name = "quantity")
	private String quantity;
	
	@Column(name = "added_date")
	private Date addDate;
	
	@Column(name = "live")
	private boolean live;
	
	@Column(name = "stock")
	private boolean stock;
	
	@Column(name = "warranty")
	private Date warranty;
	
	@Column(name = "product_image")
	private String productImage;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "category_id")
	Category category;

}

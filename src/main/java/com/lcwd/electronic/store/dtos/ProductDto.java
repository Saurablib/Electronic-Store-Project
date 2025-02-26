package com.lcwd.electronic.store.dtos;

import java.sql.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {
	
	private String productId;
	
	@NotBlank(message = "SubTitle is required !!")
	@Size(min = 4, message = "title should be minimum 4")
	private String productTitle;	
	@NotBlank(message = "product Description is rrequires !!")
	private String productDescription;	
	@NotBlank(message = "Need to add price..!!")
	private double price;
	@NotBlank(message = "Need to add discountPrice..!!")
	private double discountPrice;
	@NotBlank(message = "quantity is required..!!")
	private String quantity;
	@NotBlank(message = "addDate is required..!!")
	private Date addDate;
	@NotBlank(message = "live field is required..!!")
	private boolean live;
	@NotBlank(message = "stock field is required..!!")
	private boolean stock;	
	@NotBlank(message = "warranty is required..!!")
	private Date warranty;
	
	private String productImage;
	
	private CategoryDto category;
}

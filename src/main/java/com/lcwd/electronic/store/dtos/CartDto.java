package com.lcwd.electronic.store.dtos;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartDto {

	private String cartId;
	private Date createdAt;
	private UserDto user;
	List<CartItemDto> items = new ArrayList<>();
	private String caertCreatedDate;
}

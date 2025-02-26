package com.lcwd.electronic.store.dtos;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDto {

	private String categoryId;

	@NotBlank(message = "title Requires!!")
	private String title;

	@NotBlank(message = "description Requires!!")
	private String description;

	@NotBlank(message = "coverImage Requires!!")
	private String coverImage;
}

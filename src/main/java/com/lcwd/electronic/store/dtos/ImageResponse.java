package com.lcwd.electronic.store.dtos;

import org.springframework.http.HttpStatus;

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
public class ImageResponse {

	private String imageName;
	private String message;
	private boolean succsess;
	private HttpStatus response;	
}

package com.lcwd.electronic.store.util;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import com.lcwd.electronic.store.dtos.PageableResponse;

public class Util {
	
	public static<U,V> PageableResponse<V> pageanbleResponse(Page<U> page,Class<V> type){
		
		
		List<U> content = page.getContent();
		List<V> dtoList = content.stream().map(object -> new ModelMapper().map(object,type)).collect(Collectors.toList());
		
		PageableResponse<V> pageableResponse = new PageableResponse<>();
		pageableResponse.setContent(dtoList);
		pageableResponse.setPageNumber(page.getNumber());
		pageableResponse.setPageSize(page.getSize());
		pageableResponse.setTotalElements(page.getTotalElements());
		pageableResponse.setTotalPages(page.getTotalPages());
		pageableResponse.setLastPage(true);	
		return pageableResponse;
		
	}

}

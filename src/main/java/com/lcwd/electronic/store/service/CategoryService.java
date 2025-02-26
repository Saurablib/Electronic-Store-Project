package com.lcwd.electronic.store.service;

import com.lcwd.electronic.store.dtos.CategoryDto;
import com.lcwd.electronic.store.dtos.PageableResponse;

public interface CategoryService {
	
	//create 
	CategoryDto create(CategoryDto categoryDto);
	
	//update 
	CategoryDto update(CategoryDto categoryDto , String categoryId);
	
	//delete
	void deleteCategory(String categoryId);
	
	//all
	PageableResponse<CategoryDto> getAllCategory(int pageSize, int pageNumber, String SortBy, String SortDir);
	
	//getById
	CategoryDto getCategoryById(String categoryId);

}

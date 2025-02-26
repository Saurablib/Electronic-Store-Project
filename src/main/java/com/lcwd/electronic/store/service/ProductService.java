package com.lcwd.electronic.store.service;

import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.ProductDto;

public interface ProductService {

	
	//create
	ProductDto createProduct(ProductDto productDto);
	
	//update
	ProductDto updateProduct(ProductDto productDto, String productId);
	
	//delete
	void deleteProduct(String productId);
	
	//allProduct	
	PageableResponse<ProductDto> getAllProduct(int pageSize,int pageNumber,String SortBy,String SortDir);
	
	//get all:live
	
	PageableResponse<ProductDto> getAllLiveProducts(int pageSize,int pageNumber,String SortBy,String SortDir);
	
	//search
	PageableResponse<ProductDto> searchByTitle(String productTitle, int pageSize, int pageNumber, String sortBy,
			String sortDir);
	
	ProductDto getProductById(String productId);
	
	
	
	ProductDto createWithCategory(ProductDto productDto, String categoryId);
	
	ProductDto updateWithCategory(String categoryId, String productId);
	
	PageableResponse<ProductDto> getAllProductWithCategory(String categoryId,int pageSize,int pageNumber,String SortBy,String SortDir);
	
	

	
}

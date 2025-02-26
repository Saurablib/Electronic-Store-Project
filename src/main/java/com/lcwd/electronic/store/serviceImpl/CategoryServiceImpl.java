package com.lcwd.electronic.store.serviceImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.lcwd.electronic.store.dtos.CategoryDto;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.entities.Category;
import com.lcwd.electronic.store.exception.ResourceNotFoundException;
import com.lcwd.electronic.store.repositories.CategoryRepository;
import com.lcwd.electronic.store.service.CategoryService;
import com.lcwd.electronic.store.util.Util;

@Service
public class CategoryServiceImpl implements CategoryService  {
	
	private Logger log = LoggerFactory.getLogger(CategoryServiceImpl.class);
			
	@Autowired
	CategoryRepository categoryRepository;
	
	@Autowired
	ModelMapper mapper;
	
	
	@Value("${user.profile.image.path}")
	String uplpoadImagepath;

	@Override
	public CategoryDto create(CategoryDto categoryDto) {
		
		String categoryId  = UUID.randomUUID().toString();
		categoryDto.setCategoryId(categoryId);
		Category category = mapper.map(categoryDto, Category.class);
		Category saveCategory = categoryRepository.save(category);
		return mapper.map(saveCategory, CategoryDto.class);
	}

	@Override
	public CategoryDto update(CategoryDto categoryDto, String categoryId) {
			
		Category category = categoryRepository.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Category Noyt found By Given Id"));			
		category.setTitle(categoryDto.getTitle());
		category.setDescription(categoryDto.getDescription());
		category.setCoverImage(categoryDto.getCoverImage());
		Category upadtedCategory = categoryRepository.save(category);
		return mapper.map(upadtedCategory, CategoryDto.class);
	}
	

	@Override
	public void deleteCategory(String categoryId) {
		
		Category category = categoryRepository.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Category Noyt found By Given Id"));		
		String fullImagePath = uplpoadImagepath + category.getCoverImage();		
		try {
			Path path = Paths.get(fullImagePath);
			Files.delete(path);
		} catch (IOException e) {		
			log.info("getting Exception while delete the image ",e);
		}
		
		categoryRepository.delete(category);		
	}

	@Override
	public PageableResponse<CategoryDto> getAllCategory(int pageSize, int pageNumber, String sortBy, String sortDir) {
	
		 Sort sort = (sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
		 PageRequest pageable = PageRequest.of(pageNumber, pageSize,sort);
		 Page<Category> page = categoryRepository.findAll(pageable);
		 PageableResponse<CategoryDto> pageanbleResponse = Util.pageanbleResponse(page, CategoryDto.class);
		return pageanbleResponse;
	}

	@Override
	public CategoryDto getCategoryById(String categoryId) {

		Category category = categoryRepository.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Category Noyt found By Given Id"));		
		return mapper.map(category, CategoryDto.class);
	}

}

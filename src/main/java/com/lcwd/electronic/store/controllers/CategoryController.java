package com.lcwd.electronic.store.controllers;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.lcwd.electronic.store.dtos.ApiResponseMessage;
import com.lcwd.electronic.store.dtos.CategoryDto;
import com.lcwd.electronic.store.dtos.ImageResponse;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.ProductDto;
import com.lcwd.electronic.store.service.CategoryService;
import com.lcwd.electronic.store.service.FileService;
import com.lcwd.electronic.store.service.ProductService;

@RestController
@RequestMapping("/categories")
public class CategoryController {

	@Autowired
	CategoryService categoryService;
	
	@Autowired
	FileService fileService;
	
	@Autowired
	ProductService productService;
	
	@Value("${user.profile.image.path}")
	String imageUploadPath;

	@PostMapping("/create")
	public ResponseEntity<ApiResponseMessage> createCategory(@RequestBody CategoryDto categoryDto) {

		CategoryDto categoryData = categoryService.create(categoryDto);
		ApiResponseMessage apiResponseMessage = ApiResponseMessage.builder()
				.data(categoryData)
				.message("Category Created Successfully..")
				.succsess(true)
				.response(HttpStatus.CREATED).build();
		return new ResponseEntity<ApiResponseMessage>(apiResponseMessage, HttpStatus.OK);

	}
	
	@PutMapping("/update/{categoryId}")
	public ResponseEntity<ApiResponseMessage> updateCategory(@RequestBody CategoryDto categoryDto, @PathVariable String categoryId){
		
		CategoryDto update = categoryService.update(categoryDto, categoryId);
		ApiResponseMessage apiResponseMessage = ApiResponseMessage.builder()
				.data(update)
				.message("Category Updated Successfully..")
				.response(HttpStatus.OK)
				.succsess(true).build();
		return new ResponseEntity<ApiResponseMessage>(apiResponseMessage,HttpStatus.OK);
		
	}
	
	
	@DeleteMapping("/delete/{categoryId}")
	public ResponseEntity<ApiResponseMessage> deleteCategory(@PathVariable String categoryId){
	   categoryService.deleteCategory(categoryId);
		ApiResponseMessage apiResponseMessage = ApiResponseMessage.builder()
				.message("Category Deleted Successfully..")
				.response(HttpStatus.OK)
				.succsess(true).build();
		return new ResponseEntity<ApiResponseMessage>(apiResponseMessage,HttpStatus.OK);	
	}
	
	
	@GetMapping("/allCategory")
	public ResponseEntity<PageableResponse<CategoryDto>> getAllCategory(
			@RequestParam(value = "pageNumber",defaultValue = "0",required = false) int  pageNumber,
			@RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
			@RequestParam(value = "sortBy",defaultValue = "title",required = false) String  sortBy,
			@RequestParam(value = "sortDir",defaultValue = "asc",required = false) String sortDir)
	{		
		PageableResponse<CategoryDto> allCategory = categoryService.getAllCategory(pageSize, pageNumber, sortBy, sortDir);
		return new ResponseEntity<PageableResponse<CategoryDto>>(allCategory,HttpStatus.OK);
		
	}
	
	@GetMapping("/getcategoryById/{categoryId}")
	public ResponseEntity<ApiResponseMessage> getCategoryById(@PathVariable String categoryId) {

		ApiResponseMessage apiResponseMessage = ApiResponseMessage.builder()
				.data(categoryService.getCategoryById(categoryId))
				.response(HttpStatus.OK)
				.succsess(true)
				.build();
		return new ResponseEntity<ApiResponseMessage>(apiResponseMessage, HttpStatus.OK);
	}	
	
	@PostMapping("coverImage/{categoryId}")
	public ResponseEntity<ImageResponse> uploadUserImage(@RequestParam ("coverImage") MultipartFile image,
			@PathVariable String categoryId ) throws IOException{
		
		String coverImageName = fileService.UploadFiles(image, imageUploadPath);	
		CategoryDto category = categoryService.getCategoryById(categoryId);	
		category.setCoverImage(coverImageName);
		categoryService.update(category, categoryId);
		
		ImageResponse imageResponse = ImageResponse.builder()
				.imageName(coverImageName)
				.message("CategoryImage Uploaded Successfully")
				.succsess(true)
				.response(HttpStatus.CREATED).build();
		
		return new ResponseEntity<ImageResponse>(imageResponse,HttpStatus.OK);
		
	}
	
	@GetMapping("coverImage/{categoryId}")
	public void serveUserImage(@PathVariable String categoryId,HttpServletResponse response) throws IOException {
		
	    CategoryDto categoryDto = categoryService.getCategoryById(categoryId);
		InputStream source = fileService.getSource(imageUploadPath, categoryDto.getCoverImage());
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		StreamUtils.copy(source, response.getOutputStream());
	}
	
	@PostMapping("/{categoryId}/products")
	public ResponseEntity<ApiResponseMessage> createCategoryWithProducts(@PathVariable String categoryId, @RequestBody ProductDto productDto){
		
		ApiResponseMessage apiResponseMessage = ApiResponseMessage.builder()
				.succsess(true)
				.data(productService.createWithCategory(productDto, categoryId))
				.message("Product Created With Category!!")
				.response(HttpStatus.CREATED).build();	
		return new ResponseEntity<ApiResponseMessage>(apiResponseMessage,HttpStatus.CREATED);
	}
	
	
	@PutMapping("/{categoryId}/products/{productId}")
	public ResponseEntity<ApiResponseMessage> updateCategoryWithProduct(@PathVariable String categoryId, @PathVariable String productId){
		
		ApiResponseMessage apiResponseMessage = ApiResponseMessage.builder()
				.succsess(true)
				.data(productService.updateWithCategory(categoryId, productId))
				.message("Product Updated With Category")
				.response(HttpStatus.OK)
				.build();
		return new ResponseEntity<ApiResponseMessage>(apiResponseMessage,HttpStatus.OK);
		
	}
	
	
	@GetMapping("/{categoryId}/products")
	public ResponseEntity<PageableResponse<ProductDto>> getAllProductsInCategory(@PathVariable String categoryId,
			@RequestParam(value = "pageNumber",defaultValue = "0",required = false) int  pageNumber,
			@RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
			@RequestParam(value = "sortBy",defaultValue = "title",required = false) String  sortBy,
			@RequestParam(value = "sortDir",defaultValue = "asc",required = false) String sortDir){
		
		PageableResponse<ProductDto> allProductWithCategory = productService.getAllProductWithCategory(categoryId, pageSize, pageNumber, sortBy, sortDir);
		return new ResponseEntity<PageableResponse<ProductDto>>(allProductWithCategory,HttpStatus.OK);
		
	}
}

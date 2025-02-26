package com.lcwd.electronic.store.controllers;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import com.lcwd.electronic.store.dtos.ImageResponse;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.ProductDto;
import com.lcwd.electronic.store.service.FileService;
import com.lcwd.electronic.store.service.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController {
	
	@Autowired
	ProductService productService;
	
	@Autowired
	FileService fileService;
	
	@Value("${product.image.path}")
	String imageUploadPath;
	
	@PostMapping("/create")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponseMessage> createProduct(@RequestBody ProductDto productDto) {
		
		ApiResponseMessage apiResponseMessage = ApiResponseMessage.builder()
				.succsess(true)
				.data(productService.createProduct(productDto))
				.message("Product Created Successfully")
				.response(HttpStatus.CREATED).build();
		return new ResponseEntity<ApiResponseMessage>(apiResponseMessage, HttpStatus.OK);
	}
	
	@PutMapping("/update/{productId}")
	public ResponseEntity<ApiResponseMessage> updateProduct(@RequestBody ProductDto productDto, @PathVariable String productId){
		
		ApiResponseMessage apiResponseMessage = ApiResponseMessage.builder()
				.succsess(true)
				.data(productService.updateProduct(productDto, productId))
				.message("Product Updated Successfully")
				.response(HttpStatus.OK)
				.build();
		return new ResponseEntity<ApiResponseMessage>(apiResponseMessage,HttpStatus.OK);		
	}
	
	@DeleteMapping("/delete/{productId}")
	public ResponseEntity<ApiResponseMessage> deleteProduct(@PathVariable String productId){
		
		productService.deleteProduct(productId);
		ApiResponseMessage apiResponseMessage = ApiResponseMessage.builder()
				.succsess(true)
				.message("Product Deleted Successfully")
				.response(HttpStatus.OK)
				.build();
		return new ResponseEntity<>(apiResponseMessage,HttpStatus.OK);		
	}
	
	
	@GetMapping
	public ResponseEntity<PageableResponse<ProductDto>> getAllProducts(
			@RequestParam(value = "pageNumber",defaultValue = "0",required = false) int  pageNumber,
			@RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
			@RequestParam(value = "sortBy",defaultValue = "title",required = false) String  sortBy,
			@RequestParam(value = "sortDir",defaultValue = "asc",required = false) String sortDir
			){	
		
		PageableResponse<ProductDto> allProduct = productService.getAllProduct(pageSize, pageNumber, sortBy, sortDir);
		return new  ResponseEntity<PageableResponse<ProductDto>>(allProduct,HttpStatus.OK);
		
	}
	
	@GetMapping("/live")
	public ResponseEntity<PageableResponse<ProductDto>> getAllliveProducts(
			@RequestParam(value = "pageNumber",defaultValue = "0",required = false) int  pageNumber,
			@RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
			@RequestParam(value = "sortBy",defaultValue = "title",required = false) String  sortBy,
			@RequestParam(value = "sortDir",defaultValue = "asc",required = false) String sortDir
			){	
		
		PageableResponse<ProductDto> allProduct = productService.getAllLiveProducts(pageSize, pageNumber, sortBy, sortDir);
		return new  ResponseEntity<PageableResponse<ProductDto>>(allProduct,HttpStatus.OK);
		
	}

	@GetMapping("/search/{title}")
	public ResponseEntity<PageableResponse<ProductDto>> getProductsByTitle( @PathVariable String title,
			@RequestParam(value = "pageNumber",defaultValue = "0",required = false) int  pageNumber,
			@RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
			@RequestParam(value = "sortBy",defaultValue = "title",required = false) String  sortBy,
			@RequestParam(value = "sortDir",defaultValue = "desc",required = false) String sortDir){	
		
		PageableResponse<ProductDto> allProduct = productService.searchByTitle(title,pageSize, pageNumber, sortBy, sortDir);
		return new  ResponseEntity<PageableResponse<ProductDto>>(allProduct,HttpStatus.OK);
		
	}
	
	@GetMapping("/Id/{productId}")
	public ResponseEntity<ApiResponseMessage> getProductById(@PathVariable String productId){
		
		ApiResponseMessage response = ApiResponseMessage.builder()
				.data(productService.getProductById(productId))
				.succsess(true)
				.message("Found Product By product Id")
				.response(HttpStatus.OK).build();
		return new ResponseEntity<ApiResponseMessage>(response,HttpStatus.OK);	
	}
	
	@PostMapping("image/{productId}")
	public ResponseEntity<ImageResponse> uploadImage(@PathVariable String productId,
			@RequestParam ("productImage") MultipartFile image ) throws IOException{
		
        String imageName = fileService.UploadFiles(image, imageUploadPath);		
		ProductDto product = productService.getProductById(productId);
		product.setProductImage(imageName);
		productService.updateProduct(product, productId);	
		
		ImageResponse imageResponse = ImageResponse.builder()
				.imageName(imageName)
				.message("Image Uploaded Successfully")
				.succsess(true)
				.response(HttpStatus.CREATED).build();
		
		return new ResponseEntity<ImageResponse>(imageResponse,HttpStatus.OK);
		
	}
	
	@GetMapping("image/{productId}")
	public void serveUserImage(@PathVariable String productId,HttpServletResponse response) throws IOException {
		
		ProductDto product = productService.getProductById(productId);
		InputStream source = fileService.getSource(imageUploadPath, product.getProductImage());
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		StreamUtils.copy(source, response.getOutputStream());
	}
}

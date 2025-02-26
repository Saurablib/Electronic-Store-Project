package com.lcwd.electronic.store.serviceImpl;

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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.ProductDto;
import com.lcwd.electronic.store.entities.Category;
import com.lcwd.electronic.store.entities.Product;
import com.lcwd.electronic.store.exception.ResourceNotFoundException;
import com.lcwd.electronic.store.repositories.CategoryRepository;
import com.lcwd.electronic.store.repositories.ProductRepository;
import com.lcwd.electronic.store.service.ProductService;
import com.lcwd.electronic.store.util.Util;


@Service
public class ProductServiceImpl implements ProductService {
	
	@Autowired
	ModelMapper mapper;
	
	@Autowired
	ProductRepository productRepository;
	
	@Autowired
	CategoryRepository categoryRepository;
	
	@Value("${product.image.path}")
	String imageUploadPath;
	
	Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

	@Override
	public ProductDto createProduct(ProductDto productDto) {
	
		String productId = UUID.randomUUID().toString();
		productDto.setProductId(productId);
		Product product = mapper.map(productDto, Product.class);
		Product savedProduct = productRepository.save(product);
		return mapper.map(savedProduct, ProductDto.class);
	}

	@Override
	public ProductDto updateProduct(ProductDto productDto, String productId) {
	
		Product product = productRepository.findById(productId).orElseThrow(()-> new ResourceNotFoundException("Product Not Found With Given Id !!"));
		product.setTitle(productDto.getProductTitle());
		product.setProductDescription(productDto.getProductDescription());
		product.setPrice(productDto.getPrice());
		product.setDiscountPrice(productDto.getDiscountPrice());
		product.setQuantity(productDto.getQuantity());
		product.setAddDate(productDto.getAddDate());
		product.setLive(productDto.isLive());
		product.setStock(productDto.isStock());
		product.setWarranty(productDto.getWarranty());
		product.setProductImage(productDto.getProductImage());
		Product updateProduct = productRepository.save(product);
		return mapper.map(updateProduct, ProductDto.class);
	}

	@Override
	public void deleteProduct(String productId) {
		
		Product product = productRepository.findById(productId).orElseThrow(()-> new ResourceNotFoundException("Product Not Found With Given Id !!"));
		
		String fullImagePath =	imageUploadPath + product.getProductImage();
	    try {
			Path path = Paths.get(fullImagePath);
			Files.delete(path);
		} catch (Exception e) {
			e.printStackTrace();
		}   
		productRepository.delete(product);	
	}

	@Override
	public PageableResponse<ProductDto> getAllProduct(int pageSize,int pageNumber,String sortBy,String sortDir) {
	
		Sort sort = (sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
		Pageable pageable = PageRequest.of(pageNumber, pageSize,sort);
		Page<Product> page = productRepository.findAll(pageable);
		return Util.pageanbleResponse(page, ProductDto.class);
	}

	@Override
	public PageableResponse<ProductDto> getAllLiveProducts(int pageSize,int pageNumber,String sortBy,String sortDir) {

		Sort sort = (sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
		Pageable pageable = PageRequest.of(pageNumber, pageSize,sort);
		Page<Product> page = productRepository.findByLiveTrue(pageable);
		return Util.pageanbleResponse(page, ProductDto.class);
	}

	@Override
	public PageableResponse<ProductDto> searchByTitle( String productTitle,int pageSize,int pageNumber,String sortBy,String sortDir) {
		
		Sort sort = (sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
		Pageable pageable = PageRequest.of(pageNumber, pageSize,sort);
		Page<Product> page = productRepository.findByTitleContaining(productTitle,pageable);
		return Util.pageanbleResponse(page, ProductDto.class);
	}

	@Override
	public ProductDto getProductById(String productId) {
		
		Product product = productRepository.findById(productId).orElseThrow(()-> new ResourceNotFoundException("Product Not Found With Given Id !!"));
		return mapper.map(product, ProductDto.class);
	}

	@Override
	public ProductDto createWithCategory(ProductDto productDto, String categoryId) {
		
		Category category = categoryRepository.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Category Not Found with given Id!!"));
	
		String productId = UUID.randomUUID().toString();
		Product product = mapper.map(productDto, Product.class);	
		product.setProductId(productId);
		product.setCategory(category);
		Product savedProduct = productRepository.save(product);
		
		return mapper.map(savedProduct, ProductDto.class);

	}

	@Override
	public ProductDto updateWithCategory(String categoryId, String productId) {
		
		Product product = productRepository.findById(productId).orElseThrow(()->new ResourceNotFoundException("Product Not Found With Given Id !!"));	
		Category category = categoryRepository.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Category Not Found With given Id !!"));	
		product.setCategory(category);		
		Product saveProduct  = productRepository.save(product);
		
		return mapper.map(saveProduct, ProductDto.class);
	}

	@Override
	public PageableResponse<ProductDto> getAllProductWithCategory(String categoryId, int pageSize, int pageNumber,
			String sortBy, String sortDir) {
		
		Category category = categoryRepository.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Category Not Found With given Id !!"));	
		Sort sort = (sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);	
		Page<Product> page = productRepository.findByCategory(category,pageable);
		return Util.pageanbleResponse(page, ProductDto.class);
	}

}

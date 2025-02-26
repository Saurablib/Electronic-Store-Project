package com.lcwd.electronic.store.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lcwd.electronic.store.dtos.ProductDto;
import com.lcwd.electronic.store.entities.Category;
import com.lcwd.electronic.store.entities.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

	List<ProductDto> findByTitle(String subTitle);

	Page<Product> findByLiveTrue(Pageable pageable);

	Page<Product> findByTitleContaining(String subTitle, Pageable pageable);

	Page<Product> findByCategory(Category category, Pageable pageable);

	

}

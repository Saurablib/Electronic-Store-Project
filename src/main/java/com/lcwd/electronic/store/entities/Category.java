package com.lcwd.electronic.store.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "categories")
public class Category {
	
	@Id
	@Column(name = "category_id" )
	private String categoryId;
	
	@Column(name = "cat_title", length = 100)
	private String title;
	
	@Column(name = "cat_desc", length = 100 , nullable = false)
	private String description;
	
	@Column(name = "cover_image")
	private String coverImage;
	
	@OneToMany(mappedBy = "category",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	List<Product> productList = new ArrayList<Product>();

}

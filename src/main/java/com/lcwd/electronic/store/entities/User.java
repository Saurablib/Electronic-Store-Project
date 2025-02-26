package com.lcwd.electronic.store.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "elct_user")
@Getter
@Setter
public class User implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private int userId;

	@Column(name = "user_name", length = 30)
	private String name;

	@Column(name = "user_email", unique = true)
	private String email;

	@Column(name = "password", length = 10)
	private String password;

	@Column(name = "gender", length = 5)
	private String gender;

	@Column(name = "about", length = 100)
	private String about;

	@Column(name = "user_image_name")
	private String imageName;

	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private List<Order> order = new ArrayList<Order>();
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	Set<Role> roles = new HashSet<Role>();
	
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
	private Cart cart;
	

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

      Set<SimpleGrantedAuthority> authorities =
    		  this.roles.stream().map(role -> new SimpleGrantedAuthority(role.getRoleName())).collect(Collectors.toSet());		
      return authorities;
	}


	@Override
	public String getPassword() {
	
		return this.password;
	}

	@Override
	public boolean isAccountNonExpired() {
		
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {	
		
		return true;
	}

	@Override
	public boolean isEnabled() {
		
		return true;
	}


	@Override
	public String getUsername() {

		return this.email;
	}

}

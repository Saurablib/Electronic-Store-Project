package com.lcwd.electronic.store.service;

import java.util.List;
import java.util.Optional;

import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.entities.User;


public interface UserService {
	
	public UserDto createUser(UserDto userDto);
	
	public UserDto updateUser(UserDto userDto, Integer UserId);
	
	public void deleteUser( Integer UserId);
	
	public PageableResponse<UserDto> userList(int pageNumber, int pageSize, String sortBy, String sortDir);
	
	public UserDto getUserByid(Integer UserId);
	
	public UserDto getUserByEmail(String email);
	
	public List<UserDto> searchUser(String keyword);
	
	public Optional<User> findUserByEmailOptional(String email);


}

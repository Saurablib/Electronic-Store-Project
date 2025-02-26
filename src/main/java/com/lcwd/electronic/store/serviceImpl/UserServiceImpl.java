package com.lcwd.electronic.store.serviceImpl;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.exception.ResourceNotFoundException;
import com.lcwd.electronic.store.repositories.UserRepository;
import com.lcwd.electronic.store.service.UserService;
import com.lcwd.electronic.store.util.Util;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ModelMapper mapper;
	
	@Value("${user.profile.image.path}")
	private String imageUploadPath;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	@Override
	public UserDto createUser(UserDto userDto) {
		
		Integer userId = Math.abs(UUID.randomUUID().toString().hashCode()) ;
		userDto.setUserId(userId);
		userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
		User user = dtoToEntity(userDto);
		User saveUser = userRepository.save(user);		
		userDto = entityToDto(saveUser);
		return userDto;
	}


	@Override
	public UserDto updateUser(UserDto userDto, Integer userId) {
		
		User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("given userId not found")) ;
		
		user.setName(userDto.getName());
		user.setPassword(passwordEncoder.encode(userDto.getPassword()));
		user.setGender(userDto.getGender());
		user.setAbout(userDto.getAbout());
		user.setImageName(userDto.getImageName());
		User updatedUser = userRepository.save(user);
		
		UserDto updatedDto = entityToDto(updatedUser);
		return updatedDto;
	}

	@Override
	public void deleteUser(Integer userId) {
		
		User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("given userId not found"));
		
	    String fullImagePath =	imageUploadPath + user.getImageName();
	    try {
			Path path = Paths.get(fullImagePath);
			Files.delete(path);
		} catch (Exception e) {
			e.printStackTrace();
		}   
		userRepository.delete(user);	
	}

	@Override
	public PageableResponse<UserDto> userList(int pageNumber, int pageSize,String sortBy, String sortDir) {
		
		Sort sort = (sortDir.equalsIgnoreCase("desc")) ?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());		
		Pageable pageable =  PageRequest.of(pageNumber, pageSize,sort);
		Page<User> page = userRepository.findAll(pageable);	 	
		PageableResponse<UserDto> pageanbleResponse = Util.pageanbleResponse(page, UserDto.class);
		return pageanbleResponse;
	}

	@Override
	public UserDto getUserByid(Integer userId) {
		
		User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("given userId not found")) ;
		System.out.println("Before User Dto {} .. !!"+user);
	
		UserDto userDto = mapper.map(user,UserDto.class);
		
	
		userDto.setName(user.getName());
		
		return userDto;
	}

	@Override
	public UserDto getUserByEmail(String email) {
	
		User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Given Email is not present!!"));
		
		System.out.println("Before User Dto {} .. !!"+user);
		UserDto userDto = mapper.map(user,UserDto.class);
		userDto.setName(user.getName());
		
		return userDto;
	}

	@Override
	public List<UserDto> searchUser(String userName) {
		
		 List<User> userbyNameContaining = userRepository.findByNameContaining(userName); 
		 List<UserDto> collectUsers = userbyNameContaining.stream().map(user -> entityToDto(user)).collect(Collectors.toList());
		 return collectUsers ;
	}
	
	
	private UserDto entityToDto(User saveUser) {	
		return mapper.map(saveUser, UserDto.class);
	}

	private User dtoToEntity(UserDto userDto) {
		return mapper.map(userDto, User.class);
	}


	@Override
	public Optional<User> findUserByEmailOptional(String email) {
		
		return userRepository.findByEmail(email);
	}

}

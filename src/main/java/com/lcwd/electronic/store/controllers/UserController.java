package com.lcwd.electronic.store.controllers;


import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

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
import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.service.FileService;
import com.lcwd.electronic.store.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {
	
	@Autowired
    UserService userService;
	
	@Autowired
	FileService fileService;
	
	@Value("${user.profile.image.path}")
	private String imageUploadPath;
	
	@PostMapping("/createUser")
	 public ResponseEntity<ApiResponseMessage> createUser(@Valid @RequestBody UserDto userDto){
	 userService.createUser(userDto);
	 ApiResponseMessage apiResponseMessage = ApiResponseMessage
		 .builder()
		 .message("User Created Successfully")
		 .succsess(true).response(HttpStatus.OK)
		 .build();
		 return new ResponseEntity<>(apiResponseMessage, HttpStatus.CREATED);	 
	 }
	
	
	@PutMapping("/{userId}")
	public ResponseEntity<ApiResponseMessage> updateUser(
			@PathVariable Integer userId,
			@Valid @RequestBody  UserDto userDto ){
		
		userService.updateUser(userDto, userId);
		ApiResponseMessage apiResponseMessage = ApiResponseMessage.builder()
				.message("User Updated Successfully")
				.succsess(true)
				.response(HttpStatus.OK)
				.build();		
		return new ResponseEntity<>(apiResponseMessage, HttpStatus.OK);
		
	}
	
	@DeleteMapping("/{userId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponseMessage> deleteUser(
			@PathVariable Integer userId){
		
		userService.deleteUser(userId);
		ApiResponseMessage response = ApiResponseMessage
				.builder()
				.message("User Deleted Successfully!!"+userId)
				.succsess(true)
				.response(HttpStatus.OK).build();	
		return new ResponseEntity<>(response,HttpStatus.OK);
		
	}
	
	@GetMapping
	public ResponseEntity<PageableResponse<UserDto>> listOfUsers(
			@RequestParam(value = "pageNumber",required = false) int  pageNumber,
			@RequestParam(value = "pageSize",required = false) int pageSize,
			@RequestParam(value = "sortBy",defaultValue = "Name",required = false) String  sortBy,
			@RequestParam(value = "sortDir",defaultValue = "asc",required = false) String sortDir
			){		
		return new ResponseEntity<>(userService.userList(pageNumber, pageSize,sortBy,sortDir),HttpStatus.OK);
	}
	
	@GetMapping("/{userId}")
	public ResponseEntity<UserDto> getUserById(@PathVariable Integer userId){
	
		UserDto userByid = userService.getUserByid(userId);
		return new ResponseEntity<>(userByid,HttpStatus.OK);
		
	}
	
	@GetMapping("/email/{email}")
	public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email){
	
		return new ResponseEntity<UserDto>( userService.getUserByEmail(email),HttpStatus.OK);
	}
	
	@GetMapping("Search/{userName}")
	public ResponseEntity<List<UserDto>> searchUser(@PathVariable String userName){
		
		return new ResponseEntity<List<UserDto>>(userService.searchUser(userName),HttpStatus.OK);		
	}
	
	
	@PostMapping("image/{userId}")
	public ResponseEntity<ImageResponse> uploadUserImage(@RequestParam ("userImage") MultipartFile image,
			@PathVariable Integer userId ) throws IOException{
		
		String imageName = fileService.UploadFiles(image, imageUploadPath);
		
		UserDto user = userService.getUserByid(userId);
		user.setImageName(imageName);
		userService.updateUser(user, userId);
		
		ImageResponse imageResponse = ImageResponse.builder()
				.imageName(imageName)
				.message("Image Uploaded Successfully")
				.succsess(true)
				.response(HttpStatus.CREATED).build();
		
		return new ResponseEntity<ImageResponse>(imageResponse,HttpStatus.OK);
		
	}
	
	@GetMapping("image/{userId}")
	public void serveUserImage(@PathVariable Integer userId,HttpServletResponse response) throws IOException {
		
		UserDto user = userService.getUserByid(userId);
		InputStream source = fileService.getSource(imageUploadPath, user.getImageName());
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		StreamUtils.copy(source, response.getOutputStream());
	}
}

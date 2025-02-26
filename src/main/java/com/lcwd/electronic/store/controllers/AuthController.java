package com.lcwd.electronic.store.controllers;

import java.io.IOException;
import java.security.Principal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.lcwd.electronic.store.dtos.JwtRequest;
import com.lcwd.electronic.store.dtos.JwtResponse;
import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.exception.BadRequestException;
import com.lcwd.electronic.store.security.JwtHelper;
import com.lcwd.electronic.store.service.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {
	

	private Logger log = LoggerFactory.getLogger(AuthController.class);
	
	@Autowired
	UserDetailsService userDetailsService;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	private AuthenticationManager authenticationManager; 
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private JwtHelper jwtHelper;
	
	@Value("${googleClientId}")
	String googleClientId;
	
	@Value("${googleClientId}")
	String googlePassword;
	
	
	@PostMapping("/login")
	public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request){
		
		this.doAuthenticate(request.getEmail(),request.getPassword());
		
		UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());	
		String token = this.jwtHelper.generateToken(userDetails);
		UserDto user = modelMapper.map(userDetails, UserDto.class);
		JwtResponse jwtResponse = JwtResponse.builder().jwtToken(token).userDto(user).build();
		return new ResponseEntity<>(jwtResponse,HttpStatus.OK);	
	}
	
	
	private void doAuthenticate(String email, String password) {
			
		UsernamePasswordAuthenticationToken  authentication = new UsernamePasswordAuthenticationToken(email, password);	
		try {
			authenticationManager.authenticate(authentication);
		} catch (AuthenticationException e) {
			throw new BadRequestException("Invalid Username And Password !!");
		}
		
	}

	@GetMapping("/currentUser")
	public ResponseEntity<UserDto> getUserDetails(Principal principal){
		
		String name = principal.getName();
		return new ResponseEntity<>(modelMapper.map(userDetailsService.loadUserByUsername(name), UserDto.class),HttpStatus.OK);
		
	}
	
	
	@PostMapping("/google")
	public ResponseEntity<JwtResponse> loginWithGoogle(@RequestBody Map<String, Object> data) throws IOException{
	
		String googleTokenId = data.get("idToken").toString();		
		NetHttpTransport netHttpTransport = new NetHttpTransport();	
		JacksonFactory jacksonFactory = JacksonFactory.getDefaultInstance(); 
		GoogleIdTokenVerifier.Builder verifier = new GoogleIdTokenVerifier.Builder(netHttpTransport, jacksonFactory).setAudience(Collections.singleton(googleClientId));		
		GoogleIdToken googleIdToken = GoogleIdToken.parse(verifier.getJsonFactory(), googleTokenId);		
		Payload payload = googleIdToken.getPayload();
		
		log.info("payload :{}", payload);
		
		String email = payload.getEmail();	
		User user = null;		
		user = userService.findUserByEmailOptional(email).orElse(null);
		
		if(user == null) {			
		  user = this.saveUser(email,data.get("name").toString(),data.get("photoUrl").toString());
		}
		
		ResponseEntity<JwtResponse> jwResponseEntity = this.login(JwtRequest.builder().email(user.getEmail()).password(googlePassword).build());
		return jwResponseEntity;
		
	}


	private User saveUser(String email, String name, String photoUrl) {
		
		UserDto userDto = UserDto.builder()
				.name(name)
				.email(email)
				.password(googlePassword)
				.imageName(photoUrl)
				.roles(new HashSet<>())
				.build();
		
		UserDto user = userService.createUser(userDto);
		return modelMapper.map(user, User.class);
	
	}

}

package com.lcwd.electronic.store.dtos;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UserDto {
	
	  
	  private int userId; 
	 
	  @Size(min = 3, max = 50, message = "Invalid Name!!")
	  private String name;
	  
	  @Pattern(regexp = "^[a-zA-Z0-9]+([._-][0-9a-zA-Z]+)*@[a-zA-Z0-9]+([.-][0-9a-zA-Z]+)*\\.[a-zA-Z]{2,}$", message = "Invalid Email")
	  @NotBlank(message = "Email Is Required")
	  private String email;	
	  
	  @NotBlank(message = "Password Is Required")
	  private String password;
	  
	  @Size(min = 1, max=6, message = "Invalid gender!!")
	  private String gender;
	  
	  @NotBlank(message = "Write Somthing about yourself")
	  private String about;
	  
	  @NotBlank(message = "Image Is Required")
	  private String imageName;
	  
	  Set<RoleDto> roles = new HashSet<>();

}

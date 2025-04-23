package com.crazycoder.dto;

import java.util.List;

import com.crazycoder.model.Role;
import com.crazycoder.model.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Builder
@NoArgsConstructor
public class UserDto {

	private Integer id;
	private String firstName;
	private String lastName;
	private String email;
	private String mobNo;
	private String password;
	
	private List<RoleDto> roles;
	
	@AllArgsConstructor
	@Getter
	@Setter
	@Builder
	@NoArgsConstructor
	public static class RoleDto{
		private Integer id;
		private String name;
	}

}

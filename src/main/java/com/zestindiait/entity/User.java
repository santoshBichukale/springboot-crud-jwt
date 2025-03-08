package com.zestindiait.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
@Entity
public class User {
	@Id
	private String username;
	private String password;
	private String firstName;
	private String lastName;
	private String email;
	private String role;
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	

}

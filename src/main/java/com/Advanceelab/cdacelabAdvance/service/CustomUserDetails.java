package com.Advanceelab.cdacelabAdvance.service;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.Advanceelab.cdacelabAdvance.entity.User;

public class CustomUserDetails implements UserDetails {
	

		
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private User user;

//	private String myPassword;

	public CustomUserDetails() {
		
	}
	public CustomUserDetails(User user)
	{
		super();
		this.user=user;

		//System.out.println("\n\nFrom MyUser: Username: "+ MyUser.getUsername()+"\nPass: "+MyUser.getMyPassword());
	}
	
	public Collection<? extends GrantedAuthority> getAuthorities(){
		return Collections.singleton(new SimpleGrantedAuthority(user.getRole()));
	}
	
	public String getPassword() {
		return user.getPassword();
	}

	public String getUsername() {
		return user.getUsername();
	}
	
	public int getbatch() {
		return user.getBatch();
	}
	
	public boolean isAccountNonExpired() {
		return true;
	}
	
	public boolean isAccountNonLocked() {
		return true;
	}
	
	public boolean isCredentialsNonExpired() {
		return true;
	}
	
	public boolean isEnabled() {
		return true;
	}
	
	public User getUser() {
		return this.user;
	}
}

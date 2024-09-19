package com.Advanceelab.cdacelabAdvance.dto;

import java.time.LocalDate;

public class ELabDetails {

	String username;
	
	String password;
	
	LocalDate registrationDate;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public LocalDate getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(LocalDate registrationDate) {
		this.registrationDate = registrationDate;
	}

	public ELabDetails(String username, String password, LocalDate registrationDate) {

		this.username = username;
		this.password = password;
		this.registrationDate = registrationDate;
	}

	public ELabDetails() {

	}

	@Override
	public String toString() {
		return "ELabDetails [username=" + username + ", password=" + password + ", registrationDate=" + registrationDate
				+ "]";
	}

	
}

package com.Advanceelab.cdacelabAdvance.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="loginuser")
public class User {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	private String username;
	private String password;
	private String role;
	private int batch;
	private String emailAddress;
	private LocalDate registrationDate;
	private LocalDate approvalDate;
	private LocalDate validTill;
	
	@Column(nullable = true)
	private Integer loginAttempt;
	
	private LocalDate loginTime;
	private Boolean approved = false;
	
	public boolean isApproved() {
		return approved;
	}

	public void setApproved(boolean approved) {
		this.approved = approved;
	}

	public Integer getLoginAttempt() {
		return loginAttempt;
	}

	public void setLoginAttempt(Integer loginAttempt) {
		this.loginAttempt = loginAttempt;
	}

	


	public LocalDate getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(LocalDate loginTime) {
		this.loginTime = loginTime;
	}




	@Column(name = "hppcode")
	private String hppCode;
	public String getHppCode() {
		return hppCode;
	}

	public void setHppCode(String hppCode) {
		this.hppCode = hppCode;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}


	@Column(name = "cid")
	private String cid;
	
	
	public User() {
		
	}

	public User(int id, String username, String password, String role, int batch, String emailAddress,
			LocalDate registrationDate, LocalDate approvalDate, LocalDate validTill) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.role = role;
		this.batch = batch;
		this.emailAddress = emailAddress;
		this.registrationDate = registrationDate;
		this.approvalDate = approvalDate;
		this.validTill = validTill;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
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
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
		
	public int getBatch() {
		return batch;
	}

	public void setBatch(int batch) {
		this.batch = batch;
	}
	
	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	
	public LocalDate getRegistrationDate() {
		return registrationDate;
	}


	public void setRegistrationDate(LocalDate registrationDate) {
		this.registrationDate = registrationDate;
	}
	
	public LocalDate getValidTill() {
		return validTill;
	}

	public void setValidTill(LocalDate validTill) {
		this.validTill = validTill;
	}
    	
	public LocalDate getApprovalDate() {
		return approvalDate;
	}

	public void setApprovalDate(LocalDate approvalDate) {
		this.approvalDate = approvalDate;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", role=" + role + ", batch="
				+ batch + ", emailAddress=" + emailAddress + ", registrationDate=" + registrationDate
				+ ", approvalDate=" + approvalDate + ", validTill=" + validTill + "]";
	}

	public Integer getbatch() {
		// TODO Auto-generated method stub
		return null;
	}


	public String getRoles() {
		// TODO Auto-generated method stub
		return null;
	}


	

	
	
	
	
}

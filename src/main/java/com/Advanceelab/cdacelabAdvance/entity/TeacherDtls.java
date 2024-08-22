package com.Advanceelab.cdacelabAdvance.entity;



import java.time.LocalDate;
import java.util.Arrays;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class TeacherDtls {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String state;

	private String college;

	private String firstName;

	private String lastName;

	private String fatherName;

	private String gender;

	private LocalDate dob;

	private String mobileNumber;

	private String password;

	private String emailAddress;

	@Lob
	private byte[] IdProof=null;
	
	private LocalDate registrationDate;



	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}



	public String getState() {
		return state;
	}



	public void setState(String state) {
		this.state = state;
	}



	public String getCollege() {
		return college;
	}



	public void setCollege(String college) {
		this.college = college;
	}



	public String getFirstName() {
		return firstName;
	}



	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}



	public String getLastName() {
		return lastName;
	}



	public void setLastName(String lastName) {
		this.lastName = lastName;
	}



	public String getFatherName() {
		return fatherName;
	}



	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}



	public String getGender() {
		return gender;
	}



	public void setGender(String gender) {
		this.gender = gender;
	}



	public LocalDate getDob() {
		return dob;
	}



	public void setDob(LocalDate dob) {
		this.dob = dob;
	}



	public String getMobileNumber() {
		return mobileNumber;
	}



	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}



	public String getPassword() {
		return password;
	}



	public void setPassword(String password) {
		this.password = password;
	}



	public String EmailAddress() {
		return emailAddress;
	}



	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}



	public byte[] getIdProof() {
		return IdProof;
	}



	public void setIdProof(byte[] idProof) {
		IdProof = idProof;
	}



	public boolean isApproved() {
		return approved;
	}



	public void setApproved(boolean approved) {
		this.approved = approved;
	}



	public String getEmailAddress() {
		return emailAddress;
	}

    

	public LocalDate getRegistrationDate() {
		return registrationDate;
	}



	public void setRegistrationDate(LocalDate registrationDate) {
		this.registrationDate = registrationDate;
	}



	private boolean approved;



	@Override
	public String toString() {
		return "TeacherDtls [id=" + id + ", state=" + state + ", college=" + college + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", fatherName=" + fatherName + ", gender=" + gender + ", dob=" + dob
				+ ", mobileNumber=" + mobileNumber + ", password=" + password + ", emailAddress=" + emailAddress
				+ ", IdProof=" + Arrays.toString(IdProof) + ", registrationDate=" + registrationDate + ", approved="
				+ approved + "]";
	}

}
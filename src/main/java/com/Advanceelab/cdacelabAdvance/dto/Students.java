package com.Advanceelab.cdacelabAdvance.dto;

import java.time.LocalDate;

public class Students {
	
	private int id;
	
	private String firstName;

	private String lastName;
	
	private String fatherName;

	private String qualification;
	
	private String emailAddress;

	private String mobileNumber;
	
	private LocalDate dob;
	
	private String category;
	
	private String gender;
	
	private String state;

	private String college;
	
	private String className;

	private int batch;
	
	private LocalDate registrationDate;
	
	private LocalDate approvedDate;
	
	private LocalDate validTill;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getQualification() {
		return qualification;
	}

	public void setQualification(String qualification) {
		this.qualification = qualification;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public LocalDate getDob() {
		return dob;
	}

	public void setDob(LocalDate dob) {
		this.dob = dob;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
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

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public int getBatch() {
		return batch;
	}

	public void setBatch(int batch) {
		this.batch = batch;
	}

	public LocalDate getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(LocalDate registrationDate) {
		this.registrationDate = registrationDate;
	}

	public LocalDate getApprovedDate() {
		return approvedDate;
	}

	public void setApprovedDate(LocalDate approvedDate) {
		this.approvedDate = approvedDate;
	}

	public LocalDate getValidTill() {
		return validTill;
	}

	public void setValidTill(LocalDate validTill) {
		this.validTill = validTill;
	}

	public Students(int id, String firstName, String lastName, String fatherName, String qualification,
			String emailAddress, String mobileNumber, LocalDate dob, String category, String gender, String state,
			String college, String className, int batch, LocalDate registrationDate, LocalDate approvedDate,
			LocalDate validTill) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.fatherName = fatherName;
		this.qualification = qualification;
		this.emailAddress = emailAddress;
		this.mobileNumber = mobileNumber;
		this.dob = dob;
		this.category = category;
		this.gender = gender;
		this.state = state;
		this.college = college;
		this.className = className;
		this.batch = batch;
		this.registrationDate = registrationDate;
		this.approvedDate = approvedDate;
		this.validTill = validTill;
	}

	public Students() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "Students [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", fatherName="
				+ fatherName + ", qualification=" + qualification + ", emailAddress=" + emailAddress + ", mobileNumber="
				+ mobileNumber + ", dob=" + dob + ", category=" + category + ", gender=" + gender + ", state=" + state
				+ ", college=" + college + ", className=" + className + ", batch=" + batch + ", registrationDate="
				+ registrationDate + ", approvedDate=" + approvedDate + ", validTill=" + validTill + "]";
	}
	
}

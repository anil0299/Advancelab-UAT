package com.Advanceelab.cdacelabAdvance.dto;

public class BatchCompletionStatus {

	int stuId;
	String firstName;
	String lastName;
	String email;
	String mobileNumber;
	float basicLevelStatus;
	float eLabStatus;
	float quizStatus;
	public int getStuId() {
		return stuId;
	}
	public void setStuId(int stuId) {
		this.stuId = stuId;
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public float getBasicLevelStatus() {
		return basicLevelStatus;
	}
	public void setBasicLevelStatus(float basicLevelStatus) {
		this.basicLevelStatus = basicLevelStatus;
	}
	public float geteLabStatus() {
		return eLabStatus;
	}
	public void seteLabStatus(float eLabStatus) {
		this.eLabStatus = eLabStatus;
	}
	public float getQuizStatus() {
		return quizStatus;
	}
	public void setQuizStatus(float quizStatus) {
		this.quizStatus = quizStatus;
	}
	public BatchCompletionStatus(int stuId, String firstName, String lastName, String email, String mobileNumber,
			float basicLevelStatus, float eLabStatus, float quizStatus) {
		super();
		this.stuId = stuId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.mobileNumber = mobileNumber;
		this.basicLevelStatus = basicLevelStatus;
		this.eLabStatus = eLabStatus;
		this.quizStatus = quizStatus;
	}
	
	public BatchCompletionStatus() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "BatchCompletionStatus [stuId=" + stuId + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", email=" + email + ", mobileNumber=" + mobileNumber + ", basicLevelStatus=" + basicLevelStatus
				+ ", eLabStatus=" + eLabStatus + ", quizStatus=" + quizStatus + "]";
	}
	
	
	
}

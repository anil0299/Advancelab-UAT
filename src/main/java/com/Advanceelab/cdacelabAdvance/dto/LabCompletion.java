package com.Advanceelab.cdacelabAdvance.dto;


public class LabCompletion {
	
	private int id;

	private String firstName;

	private String lastName;
	
	private String emailAddress;
	
	private String college;
	
	private String state;

	private double completion;

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

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getCollege() {
		return college;
	}

	public void setCollege(String college) {
		this.college = college;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public double getCompletion() {
		return completion;
	}

	public void setCompletion(double completion) {
		this.completion = completion;
	}

	public LabCompletion(int id, String firstName, String lastName, String emailAddress, String college, String state,
			double completion) {

		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailAddress = emailAddress;
		this.college = college;
		this.state = state;
		this.completion = completion;
	}

	public LabCompletion() {

	}

	@Override
	public String toString() {
		return "LabCompletion [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", emailAddress="
				+ emailAddress + ", college=" + college + ", state=" + state + ", completion=" + completion + "]";
	}
	
}

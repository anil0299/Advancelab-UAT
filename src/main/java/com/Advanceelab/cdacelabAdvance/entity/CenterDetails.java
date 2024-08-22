package com.Advanceelab.cdacelabAdvance.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CenterDetails")
public class CenterDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	
	
	private String College;
	
	private String Address;
	
	private String State;
	
	private String City;
	
	private String Pincode;
	
	private String ContactNumber;
	
	private String EmailAddress;
	
	private String ContactName;
	
	private String ContactMobileNumber; 
	
	private String website;
	
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCollege() {
		return College;
	}

	public void setCollege(String college) {
		College = college;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public String getState() {
		return State;
	}

	public void setState(String state) {
		State = state;
	}

	public String getCity() {
		return City;
	}

	public void setCity(String city) {
		City = city;
	}

	public String getPincode() {
		return Pincode;
	}

	public void setPincode(String pincode) {
		Pincode = pincode;
	}

	public String getContactNumber() {
		return ContactNumber;
	}

	public void setContactNumber(String contactNumber) {
		ContactNumber = contactNumber;
	}

	public String getEmailAddress() {
		return EmailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		EmailAddress = emailAddress;
	}

	public String getContactName() {
		return ContactName;
	}

	public void setContactName(String contactName) {
		ContactName = contactName;
	}

	

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getContactMobileNumber() {
		return ContactMobileNumber;
	}

	public void setContactMobileNumber(String contactMobileNumber) {
		ContactMobileNumber = contactMobileNumber;
	}

	@Override
	public String toString() {
		return "CenterDetails [id=" + id + ", College=" + College + ", Address=" + Address + ", State=" + State
				+ ", City=" + City + ", Pincode=" + Pincode + ", ContactNumber=" + ContactNumber + ", EmailAddress="
				+ EmailAddress + ", ContactName=" + ContactName + ", ContactMobileNumber=" + ContactMobileNumber
				+ ", website=" + website + "]";
	}


	

	
	
	

}

package com.Advanceelab.cdacelabAdvance.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.validation.constraints.Min;

@Entity
public class StudentDtls {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String state;

	private String college;

	private String qualification;

	private String gender;

	private String firstName;

	private String lastName;

	private String fatherName;

	private String category;

	private LocalDate dob;

	private String emailAddress;

	private String mobileNumber;

	private String password;

	@Lob
	private byte[] categoryCertificate = null;

	@Lob
	private byte[] lastMarksheet = null;

	private LocalDate registrationDate;

	private boolean approved;

	private String className;

	private int batch;

	private String role;

	private LocalDate validTill;

	private LocalDate approvedDate;

	private String labemail;
	
	
	@Column(name = "hppcode")
	private String hppCode;
	@Column(name = "cid")
	private String cid;
	
	
	
	public int getBatch() {
		return batch;
	}

	public void setBatch(int batch) {
		this.batch = batch;
	}

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

	public String getQualification() {
		return qualification;
	}

	public void setQualification(String qualification) {
		this.qualification = qualification;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
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

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public LocalDate getDob() {
		return dob;
	}

	public void setDob(LocalDate dob) {
		this.dob = dob;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public byte[] getCategoryCertificate() {
		return categoryCertificate;
	}

	public void setCategoryCertificate(byte[] categoryCertificate) {
		this.categoryCertificate = categoryCertificate;
	}

	public byte[] getLastMarksheet() {
		return lastMarksheet;
	}

	public void setLastMarksheet(byte[] lastMarksheet) {
		this.lastMarksheet = lastMarksheet;
	}

	public LocalDate getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(LocalDate registrationDate) {
		this.registrationDate = registrationDate;
	}

	public boolean isApproved() {
		return approved;
	}

	public void setApproved(boolean approved) {
		this.approved = approved;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public LocalDate getValidTill() {
		return validTill;
	}

	public void setValidTill(LocalDate validTill) {
		this.validTill = validTill;
	}

	public LocalDate getApprovedDate() {
		return approvedDate;
	}

	public void setApprovedDate(LocalDate approvedDate) {
		this.approvedDate = approvedDate;
	}

	public StudentDtls(byte[] categoryCertificate, byte[] lastMarksheet) {
		super();
		this.categoryCertificate = categoryCertificate;
		this.lastMarksheet = lastMarksheet;
	}

	public StudentDtls(String className) {
		super();
		this.className = className;
	}

	public StudentDtls() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * public Integer getbatch() { // TODO Auto-generated method stub return null; }
	 */

	public Object getStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setIdProof(byte[] idProof) {
		// TODO Auto-generated method stub

	}

	public User getUser() {
		// TODO Auto-generated method stub
		return null;
	}

//	public void setBatch(@Min(1) int batch2) { // TODO Auto-generated method stub
//
//	}


	public String getLabemail() {
		return labemail;
	}

	public void setLabemail(String labemail) {
		this.labemail = labemail;
	}


}

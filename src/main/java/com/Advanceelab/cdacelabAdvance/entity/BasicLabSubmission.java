package com.Advanceelab.cdacelabAdvance.entity;

import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class BasicLabSubmission {

	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="exsubmit_id")
	private Long exsubmit_id;
	
	@Column(name="userid")
	private Long userid;
	
	@Column(name="exer_id")
	private Long exer_id;

	@Column(name="username")
	private String username;
	
	
	@Column(name="pdfname")
	private String pdfname;
	
	@Lob
	private byte[] submission_pdf=null;

	public Long getExsubmit_id() {
		return exsubmit_id;
	}

	public void setExsubmit_id(Long exsubmit_id) {
		this.exsubmit_id = exsubmit_id;
	}

	public Long getUserid() {
		return userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public Long getExer_id() {
		return exer_id;
	}

	public void setExer_id(Long exer_id) {
		this.exer_id = exer_id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPdfname() {
		return pdfname;
	}

	public void setPdfname(String pdfname) {
		this.pdfname = pdfname;
	}

	public byte[] getSubmission_pdf() {
		return submission_pdf;
	}

	public void setSubmission_pdf(byte[] submission_pdf) {
		this.submission_pdf = submission_pdf;
	}

	@Override
	public String toString() {
		return "BasicLabSubmission [exsubmit_id=" + exsubmit_id + ", userid=" + userid + ", exer_id=" + exer_id
				+ ", username=" + username + ", pdfname=" + pdfname + ", submission_pdf="
				+ Arrays.toString(submission_pdf) + "]";
	}
	
	
}

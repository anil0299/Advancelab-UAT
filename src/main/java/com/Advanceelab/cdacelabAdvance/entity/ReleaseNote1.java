package com.Advanceelab.cdacelabAdvance.entity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class ReleaseNote1 {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
		
	private String note1;
	
	private LocalDate dob;
	
	private LocalTime time;
	
	@Lob
    private byte[] pdfData =null;
	
	@Column(name = "hppcode")
	private String hppCode;
	
	@Column(name = "cid")
	private String cid;
		
	public String getFormattedDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = dob.format(formatter);
        String formattedTime = time.format(formatter);
        return formattedDate + " " + formattedTime;
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNote1() {
		return note1;
	}

	public void setNote1(String note1) {
		this.note1 = note1;
	}

	public LocalDate getDob() {
		return dob;
	}

	public void setDob(LocalDate dob) {
		this.dob = dob;
	}

	public LocalTime getTime() {
		return time;
	}

	public void setTime(LocalTime time) {
		this.time = time;
	}

	public byte[] getPdfData() {
		return pdfData;
	}

	public void setPdfData(byte[] pdfData) {
		this.pdfData = pdfData;
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

	@Override
	public String toString() {
		return "ReleaseNote1 [id=" + id + ", note1=" + note1 + ", dob=" + dob + ", time=" + time + ", pdfData="
				+ Arrays.toString(pdfData) + ", hppCode=" + hppCode + ", cid=" + cid + "]";
	}

}

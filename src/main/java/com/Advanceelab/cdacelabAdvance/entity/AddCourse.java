package com.Advanceelab.cdacelabAdvance.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Addcourse")
public class AddCourse {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	
	
	private String course;

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



	public String getCourse() {
		return course;
	}



	public void setCourse(String course) {
		this.course = course;
	}



	@Override
	public String toString() {
		return "AddCourse [id=" + id + ", course=" + course + "]";
	}
	
	

}

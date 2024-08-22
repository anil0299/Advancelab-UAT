package com.Advanceelab.cdacelabAdvance.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

@EnableAutoConfiguration
@Entity
@Table(name = "ClassExercise")
public class ClassExercise {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="class_id")
	private Integer class_id;
	
	

	@Column(name="class_name")
	private String class_name;
	
	@Column(name="exer_name")
	private String exer_name;
	
	
	@Column(name="exer_id")
	private long exer_id;
	
	
	@Column(name = "hppcode")
	private String hppCode;
	@Column(name = "cid")
	private String cid;



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


	public Integer getClass_id() {
		return class_id;
	}


	public void setClass_id(Integer class_id) {
		this.class_id = class_id;
	}


	public String getClass_name() {
		return class_name;
	}


	public void setClass_name(String class_name) {
		this.class_name = class_name;
	}


	public String getExer_name() {
		return exer_name;
	}


	public void setExer_name(String exer_name) {
		this.exer_name = exer_name;
	}


	public long getExer_id() {
		return exer_id;
	}


	public void setExer_id(long exer_id2) {
		this.exer_id = exer_id2;
	}


	@Override
	public String toString() {
		return "ClassExercise [class_id=" + class_id + ", class_name=" + class_name + ", exer_name=" + exer_name
				+ ", exer_id=" + exer_id + "]";
	}
	
	
}

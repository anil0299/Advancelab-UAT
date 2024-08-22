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
@Table(name = "batch_master")
public class Batch_Master {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="batch_id")
	private Integer batch_id;
	
	@Column(name="exer_name")
	private String exer_name;
	
	@Column(name="batch_no")
	private Integer batch_no;
	
	@Column(name="exer_id")
	private Integer exer_id;
	
	@Column(name = "hppcode")
	private String hppCode;
	
	@Column(name = "cid")
	private String cid;

	public Integer getBatch_id() {
		return batch_id;
	}

	public void setBatch_id(Integer batch_id) {
		this.batch_id = batch_id;
	}

	public String getExer_name() {
		return exer_name;
	}

	public void setExer_name(String exer_name) {
		this.exer_name = exer_name;
	}

	public Integer getBatch_no() {
		return batch_no;
	}

	public void setBatch_no(Integer batch_no) {
		this.batch_no = batch_no;
	}

	public Integer getExer_id() {
		return exer_id;
	}

	public void setExer_id(Integer exer_id) {
		this.exer_id = exer_id;
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
		return "Batch_Master [batch_id=" + batch_id + ", exer_name=" + exer_name + ", batch_no=" + batch_no
				+ ", exer_id=" + exer_id + ", hppCode=" + hppCode + ", cid=" + cid + "]";
	}
	
}

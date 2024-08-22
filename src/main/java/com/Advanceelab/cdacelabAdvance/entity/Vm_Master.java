package com.Advanceelab.cdacelabAdvance.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.transaction.annotation.Transactional;

@Entity
@Transactional
@Table(name="vm_master")
public class Vm_Master {

	
	
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	@Column(name="vm_id")
	private Long Vm_id;
	
	@Column
	private String Vm_uuid;
	
	@Column
	private String Vm_name;
	
	
	  @ManyToOne
	    @JoinColumn(name = "exercise_id")
	    private Exercise_Master exercise;
	  
	  
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


	public Long getVm_id() {
		return Vm_id;
	}


	public void setVm_id(Long vm_id) {
		Vm_id = vm_id;
	}


	public String getVm_uuid() {
		return Vm_uuid;
	}


	public void setVm_uuid(String vm_uuid) {
		Vm_uuid = vm_uuid;
	}


	public String getVm_name() {
		return Vm_name;
	}


	public void setVm_name(String vm_name) {
		Vm_name = vm_name;
	}


	public Exercise_Master getExercise() {
		return exercise;
	}


	public void setExercise(Exercise_Master exercise) {
		this.exercise = exercise;
	}


	@Override
	public String toString() {
		return "Vm_Master [Vm_id=" + Vm_id + ", Vm_uuid=" + Vm_uuid + ", Vm_name=" + Vm_name + ", exercise=" + exercise
				+ ", hppCode=" + hppCode + ", cid=" + cid + "]";
	}
	
	
	
	
}

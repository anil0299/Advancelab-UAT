package com.Advanceelab.cdacelabAdvance.service;

import java.util.List;
import java.util.Map;

import com.Advanceelab.cdacelabAdvance.entity.StudentDtls;

public class ReturnObject {

	 private List<StudentDtls> students;
	 private Map<String, String> mp;
	 

	 //parameterize constructor
	 
	 
	 public ReturnObject(List<StudentDtls> students, Map<String, String> mp) {
		
		this.students = students;
		this.mp = mp;
	}
	//getter and setter
	 
	public List<StudentDtls> getStudents() {
		return students;
	}
	public void setStudents(List<StudentDtls> students) {
		this.students = students;
	}
	public Map<String, String> getMp() {
		return mp;
	}
	public void setMp(Map<String, String> mp) {
		this.mp = mp;
	}
	    
	
	 
	 
}

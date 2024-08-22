package com.Advanceelab.cdacelabAdvance.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="student_video_watch_inf")
public class StudentVideoWatchInf {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(name="user_id")
	private int userId;
	@Column(name = "course_id")
	private int courseId;
	
	@Column(name = "course_name")
	private String courseName;
	
	@Column(name = "first_name")
	private String firstName;
	@Column(name = "last_name")
	private String lastName;
	private String email;
	private String college;
	private String state;
	private int batch;
	@Column(name="percetage_status")
	private double percetageStatus;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getCourseId() {
		return courseId;
	}
	public void setCourseId(int courseId) {
		this.courseId = courseId;
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
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
	public int getBatch() {
		return batch;
	}
	public void setBatch(int batch) {
		this.batch = batch;
	}
	public double getPercetageStatus() {
		return percetageStatus;
	}
	public void setPercetageStatus(double percetageStatus) {
		this.percetageStatus = percetageStatus;
	}
	
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	@Override
	public String toString() {
		return "StudentVideoWatchInf [id=" + id + ", userId=" + userId + ", courseId=" + courseId + ", courseName="
				+ courseName + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + ", college="
				+ college + ", state=" + state + ", batch=" + batch + ", percetageStatus=" + percetageStatus + "]";
	}
	
	
}
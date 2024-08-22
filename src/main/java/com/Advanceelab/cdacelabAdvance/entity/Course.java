package com.Advanceelab.cdacelabAdvance.entity;

import java.util.ArrayList;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name = "p_course")
public class Course {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "course_name")
	private String courseName;

	@OneToOne(cascade = { CascadeType.REFRESH, CascadeType.DETACH })
	@JoinColumn(name = "category_id")
	private CourseCategory courseCategoryID;

	@Column(name = "course_short_name")
	private String courseShortName;

	@Column(name = "course_description")
	private String description;

	@Column(name = "course_prerequisite")
	private String coursePrequisite;

	@Column(name = "course_icon")
	private String courseIcon;
	
	
	@Column(name = "created_on")
	@Temporal(TemporalType.TIMESTAMP)
	private Date created_on;

	@Column(name = "updated_on")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updated_on;
	
	@Transient
	private ArrayList<CourseTopics> topics;
	
	@Transient
	private ArrayList<Quiz> courseQuizzes;
	
	@Transient
	private boolean accessGranted;
	
	@Column(name = "hppcode")
	private String hppCode;
	@Column(name = "cid")
	private String cid;
	@Column(name = "csrfvalue")
	private String csrfValue;

	public Course() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public CourseCategory getCourseCategoryID() {
		return courseCategoryID;
	}

	public void setCourseCategoryID(CourseCategory courseCategoryID) {
		this.courseCategoryID = courseCategoryID;
	}

	public String getCourseShortName() {
		return courseShortName;
	}

	public void setCourseShortName(String courseShortName) {
		this.courseShortName = courseShortName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCoursePrequisite() {
		return coursePrequisite;
	}

	public void setCoursePrequisite(String coursePrequisite) {
		this.coursePrequisite = coursePrequisite;
	}

	public String getCourseIcon() {
		return courseIcon;
	}

	public void setCourseIcon(String courseIcon) {
		this.courseIcon = courseIcon;
	}

	public Date getCreated_on() {
		return created_on;
	}

	public void setCreated_on(Date created_on) {
		this.created_on = created_on;
	}

	public Date getUpdated_on() {
		return updated_on;
	}

	public void setUpdated_on(Date updated_on) {
		this.updated_on = updated_on;
	}
	
	
	public ArrayList<CourseTopics> getTopics() {
		return topics;
	}
	public void setTopics(ArrayList<CourseTopics> topics) {
		this.topics = topics;
	}
	
	

	public ArrayList<Quiz> getCourseQuizzes() {
		return courseQuizzes;
	}
	public void setCourseQuizzes(ArrayList<Quiz> coursequizzes) {
		this.courseQuizzes = coursequizzes;
	}
	
	public boolean isAccessGranted() {
		return accessGranted;
	}
	public void setAccessGranted(boolean accessGranted) {
		this.accessGranted = accessGranted;
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

	public String getCsrfValue() {
		return csrfValue;
	}

	public void setCsrfValue(String csrfValue) {
		this.csrfValue = csrfValue;
	}

	@Override
	public String toString() {
		return "Course [id=" + id + ", courseName=" + courseName + ", courseCategoryID=" + courseCategoryID
				+ ", courseShortName=" + courseShortName + ", description=" + description + ", coursePrequisite="
				+ coursePrequisite + ", courseIcon=" + courseIcon + ", created_on=" + created_on + ", updated_on="
				+ updated_on + "]\n";
	}

}


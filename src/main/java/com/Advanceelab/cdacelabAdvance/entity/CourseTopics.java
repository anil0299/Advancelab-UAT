package com.Advanceelab.cdacelabAdvance.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@Entity
@Table(name = "p_course_topics")
public class CourseTopics {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "topic_name")
	@NotNull(message = "is required")
	@Size(min = 1, message = "is required")
	@Pattern(regexp = "^[a-zA-Z_ ]*$", message = "Only characters are allowed")
	private String topicName;

	@Column(name = "topic_description")
	@NotNull(message = "is required")
	@Size(min = 1, message = "is required")
	@Pattern(regexp = "^[a-zA-Z_ ]*$", message = "Only characters are allowed")
	private String topicDescription;

	@Column(name = "courseid")
	private int course;

	@Column(name = "created_on")
	private Date topicCreationDate;

	@Column(name = "updated_on")
	private Date topicUpdationDate;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTopicName() {
		return topicName;
	}

	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}

	public String getTopicDescription() {
		return topicDescription;
	}

	public void setTopicDescription(String topicDescription) {
		this.topicDescription = topicDescription;
	}

	public int getCourse() {
		return course;
	}

	public void setCourse(int course) {
		this.course = course;
	}

	public Date getTopicCreationDate() {
		return topicCreationDate;
	}

	public void setTopicCreationDate(Date topicCreationDate) {
		this.topicCreationDate = topicCreationDate;
	}

	public Date getTopicUpdationDate() {
		return topicUpdationDate;
	}

	public void setTopicUpdationDate(Date topicUpdationDate) {
		this.topicUpdationDate = topicUpdationDate;
	}

	@Override
	public String toString() {
		return "CourseTopics [id=" + id + ", topicName=" + topicName + ", topicDescription=" + topicDescription
				+ ", course=" + course + ", topicCreationDate=" + topicCreationDate + ", topicUpdationDate="
				+ topicUpdationDate + "]";
	}

}
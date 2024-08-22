package com.Advanceelab.cdacelabAdvance.entity;

import java.util.Date;

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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;



@Entity
@Table(name = "p_quiz")
public class Quiz {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@OneToOne
	@JoinColumn(name = "hostid")
	private StudentDtls hostid;
	
	@OneToOne
	@JoinColumn(name = "course_id")
	private Course courseid;
	
	@Column(name="title")
	@NotNull(message = "is required")
	@Size(min = 1, message = "is required")
	@Pattern(regexp = "^[a-zA-Z0-9_ ]*$", message = "Only characters and numbers are allowed")
	private String quizTitle;
	
	@Column(name="meta_title")
	@NotNull(message = "is required")
	@Size(min = 1, message = "is required")
	@Pattern(regexp = "^[a-zA-Z0-9_ ]*$", message = "Only characters and numbers are allowed")
	private String quizMetaTitle;
	
	@Column(name="summary")
	@NotNull(message = "is required")
	@Size(min = 1, message = "is required")
	@Pattern(regexp = "^[a-zA-Z0-9_ ]*$", message = "Only characters and numbers are allowed")
	private String quizSummary;

	@Column(name = "published")
	private boolean published; 
	
	@Column(name="created_at")
	@Temporal(TemporalType.TIMESTAMP)
	private Date created_at;
	
	@Column(name="updated_at")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updated_at;
	
	@Column(name = "published_at")
	@Temporal(TemporalType.TIMESTAMP)
	private Date published_at;
	
	@Column(name = "unpublished_at")
	@Temporal(TemporalType.TIMESTAMP)
	private Date unpublished_at;
	@Column(name="created_by")
	private String createdBy;
	

	@Transient
	private QuizMeta quizMeta;
	
	@Transient
	private String courseName;
	
	@Transient
	private int userScore;
	
	@Transient
	private int totalScore;
	
	@Transient
	private boolean isAttempted;

	public Quiz() {
	}

	public Quiz(StudentDtls hostid, Course courseid,
			@NotNull(message = "is required") @Size(min = 1, message = "is required") 	@Pattern(regexp = "^[a-zA-Z0-9_ ]*$", message = "Only characters and numbers are allowed") String quizTitle,
			@NotNull(message = "is required") @Size(min = 1, message = "is required")	@Pattern(regexp = "^[a-zA-Z0-9_ ]*$", message = "Only characters and numbers are allowed") String quizMetaTitle,
			@NotNull(message = "is required") @Size(min = 1, message = "is required") 	@Pattern(regexp = "^[a-zA-Z0-9_ ]*$", message = "Only characters and numbers are allowed") String quizSummary,
			boolean published, Date created_at, Date updated_at, Date published_at, Date unpublished_at) {
		this.hostid = hostid;
		this.courseid = courseid;
		this.quizTitle = quizTitle;
		this.quizMetaTitle = quizMetaTitle;
		this.quizSummary = quizSummary;
		this.published = published;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.published_at = published_at;
		this.unpublished_at = unpublished_at;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public StudentDtls getHostid() {
		return hostid;
	}

	public void setHostid(StudentDtls studentDtls) {
		this.hostid = studentDtls;
	}

	public Course getCourseid() {
		return courseid;
	}

	public void setCourseid(Course courseid) {
		this.courseid = courseid;
	}

	public String getQuizTitle() {
		return quizTitle;
	}

	public void setQuizTitle(String quizTitle) {
		this.quizTitle = quizTitle;
	}

	public String getQuizMetaTitle() {
		return quizMetaTitle;
	}

	public void setQuizMetaTitle(String quizMetaTitle) {
		this.quizMetaTitle = quizMetaTitle;
	}

	public String getQuizSummary() {
		return quizSummary;
	}

	public void setQuizSummary(String quizSummary) {
		this.quizSummary = quizSummary;
	}

	public boolean isPublished() {
		return published;
	}

	public void setPublished(boolean published) {
		this.published = published;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	public Date getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;
	}

	public Date getPublished_at() {
		return published_at;
	}

	public void setPublished_at(Date published_at) {
		this.published_at = published_at;
	}
	
	public Date getUnpublished_at() {
		return unpublished_at;
	}

	public void setUnpublished_at(Date unpublished_at) {
		this.unpublished_at = unpublished_at;
	}
	
	
	
	public QuizMeta getQuizMeta() {
		return quizMeta;
	}
	public void setQuizMeta(QuizMeta quizMeta) {
		this.quizMeta = quizMeta;
	}	

	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}	
	
	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}


	public int getUserScore() {
		return userScore;
	}
	public void setUserScore(int userScore) {
		this.userScore = userScore;
	}

	public int getTotalScore() {
		return totalScore;
	}
	public void setTotalScore(int totalScore) {
		this.totalScore = totalScore;
	}	
	
	public boolean isAttempted() {
		return isAttempted;
	}
	public void setAttempted(boolean isAttempted) {
		this.isAttempted = isAttempted;
	}

	@Override
	public String toString() {
		return "Quiz [id=" + id + ", hostid=" + hostid + ", courseid=" + courseid + ", quizTitle=" + quizTitle
				+ ", quizMetaTitle=" + quizMetaTitle + ", quizSummary=" + quizSummary + ", published=" + published
				+ ", created_at=" + created_at + ", updated_at=" + updated_at + ", published_at=" + published_at
				+ ", unpublished_at=" + unpublished_at + "]";
	}
	
}

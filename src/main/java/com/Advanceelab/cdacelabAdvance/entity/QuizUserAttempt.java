package com.Advanceelab.cdacelabAdvance.entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "p_quiz_user_attempt")
public class QuizUserAttempt {
	
	@Id	
	@Column(name = "id")
	private int id;
		
	@Column(name = "quiz_id")
	private int quizId;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public StudentDtls student;
		
	@Column(name = "score")
	private int score;
	
	@Column(name = "published")
	private boolean published;
	
	@Column(name = "accessed_at")
	private Date accessedDate;
	
	@Column(name = "started_at")
	private Date startedDate;
	
	@Column(name = "finished_at")
	private Date finishedDate;
	
	@Column(name = "attempt")
	private int attempt;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getQuizId() {
		return quizId;
	}

	public void setQuizId(int quizId) {
		this.quizId = quizId;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public boolean isPublished() {
		return published;
	}

	public void setPublished(boolean published) {
		this.published = published;
	}

	public Date getAccessedDate() {
		return accessedDate;
	}

	public void setAccessedDate(Date accessedDate) {
		this.accessedDate = accessedDate;
	}

	public Date getStartedDate() {
		return startedDate;
	}

	public void setStartedDate(Date startedDate) {
		this.startedDate = startedDate;
	}

	public Date getFinishedDate() {
		return finishedDate;
	}

	public void setFinishedDate(Date finishedDate) {
		this.finishedDate = finishedDate;
	}

	public int getAttempt() {
		return attempt;
	}

	public void setAttempt(int attempt) {
		this.attempt = attempt;
	}

	public StudentDtls getStudent() {
		return student;
	}

	public void setStudent(StudentDtls student) {
		this.student = student;
	}

	@Override
	public String toString() {
		return "QuizUserAttempt [id=" + id + ", quizId=" + quizId + ", student=" + student + ", score=" + score
				+ ", published=" + published + ", accessedDate=" + accessedDate + ", startedDate=" + startedDate
				+ ", finishedDate=" + finishedDate + ", attempt=" + attempt + "]";
	}

}

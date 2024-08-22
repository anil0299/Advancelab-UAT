package com.Advanceelab.cdacelabAdvance.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "p_quiz_meta")
public class QuizMeta {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@OneToOne
	@JoinColumn(name = "quiz_id")
	private Quiz quizid;

	@Column(name = "content")
	private String quizTitle;

	public QuizMeta() {
	}
	
	public QuizMeta(Quiz quizid, String quizTitle) {
		this.quizid = quizid;
		this.quizTitle = quizTitle;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Quiz getQuizid() {
		return quizid;
	}

	public void setQuizid(Quiz quizid) {
		this.quizid = quizid;
	}

	public String getQuizTitle() {
		return quizTitle;
	}

	public void setQuizTitle(String quizTitle) {
		this.quizTitle = quizTitle;
	}

	@Override
	public String toString() {
		return "QuizMeta [id=" + id + ", quizid=" + quizid + ", quizTitle=" + quizTitle + "]";
	}
	
}


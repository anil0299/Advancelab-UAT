package com.Advanceelab.cdacelabAdvance.dto;

import java.util.Map;

public class QuizCompletion {

	private int studentId;
	
	private String studentName;
	
	private String emailAddress;
	
	private Map<String, Boolean> quizSubmission;

	public int getStudentId() {
		return studentId;
	}

	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public Map<String, Boolean> getQuizSubmission() {
		return quizSubmission;
	}

	public void setQuizSubmission(Map<String, Boolean> quizSubmission) {
		this.quizSubmission = quizSubmission;
	}

	public QuizCompletion(int studentId, String studentName, String emailAddress, Map<String, Boolean> quizSubmission) {

		this.studentId = studentId;
		this.studentName = studentName;
		this.emailAddress = emailAddress;
		this.quizSubmission = quizSubmission;
	}

	public QuizCompletion() {

	}

	@Override
	public String toString() {
		return "QuizCompletion [studentId=" + studentId + ", studentName=" + studentName + ", emailAddress="
				+ emailAddress + ", quizSubmission=" + quizSubmission + "]";
	}
	
}

package com.Advanceelab.cdacelabAdvance.dto;

public class AdvanceLabSubmission {

	String name;
	String email;
	String exerciseName;
	String pdfName;
	Long exerciseSubmitId;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getExerciseName() {
		return exerciseName;
	}
	
	public void setExerciseName(String exerciseName) {
		this.exerciseName = exerciseName;
	}
	
	public String getPdfName() {
		return pdfName;
	}
	
	public void setPdfName(String pdfName) {
		this.pdfName = pdfName;
	}
	
	public Long getExerciseSubmitId() {
		return exerciseSubmitId;
	}
	
	public void setExerciseSubmitId(Long exerciseSubmitId) {
		this.exerciseSubmitId = exerciseSubmitId;
	}
	
	public AdvanceLabSubmission(String name, String email, String exerciseName, String pdfName, Long exerciseSubmitId) {

		this.name = name;
		this.email = email;
		this.exerciseName = exerciseName;
		this.pdfName = pdfName;
		this.exerciseSubmitId = exerciseSubmitId;
	}
	
	public AdvanceLabSubmission() {

	}
	
	@Override
	public String toString() {
		return "AdvanceLabSubmission [name=" + name + ", email=" + email + ", exerciseName=" + exerciseName
				+ ", pdfName=" + pdfName + ", exerciseSubmitId=" + exerciseSubmitId + "]";
	}
			
}

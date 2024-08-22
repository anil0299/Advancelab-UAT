package com.Advanceelab.cdacelabAdvance.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ExerciseContent {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private long exerciseId;
	@Column(length = 10485760)
	private String exerciseHeadingName;
	@Column(length = 10485760)
	private String title;
	@Column(length = 10485760)
	private String learningObjective;
	@Column(length = 10485760)
	private String description;
	@Column(length = 10485760)
	private String labInfraStructure;
	@Column(length = 10485760)
	private String attackProcedure;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getExerciseId() {
		return exerciseId;
	}
	public void setExerciseId(long exerciseId) {
		this.exerciseId = exerciseId;
	}
	public String getExerciseHeadingName() {
		return exerciseHeadingName;
	}
	public void setExerciseHeadingName(String exerciseHeadingName) {
		this.exerciseHeadingName = exerciseHeadingName;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getLearningObjective() {
		return learningObjective;
	}
	public void setLearningObjective(String learningObjective) {
		this.learningObjective = learningObjective;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getLabInfraStructure() {
		return labInfraStructure;
	}
	public void setLabInfraStructure(String labInfraStructure) {
		this.labInfraStructure = labInfraStructure;
	}
	public String getAttackProcedure() {
		return attackProcedure;
	}
	public void setAttackProcedure(String attackProcedure) {
		this.attackProcedure = attackProcedure;
	}
	@Override
	public String toString() {
		return "ExerciseContent [id=" + id + ", exerciseId=" + exerciseId + ", exerciseHeadingName="
				+ exerciseHeadingName + ", title=" + title + ", learningObjective=" + learningObjective
				+ ", description=" + description + ", labInfraStructure=" + labInfraStructure + ", attackProcedure="
				+ attackProcedure + "]";
	}
	
	
	
}

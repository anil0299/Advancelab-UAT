package com.Advanceelab.cdacelabAdvance.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
@EnableAutoConfiguration
@Entity
@Table(name = "exercise")
public class Exercise_Master {

	private static final long serialVersionUID =1L;
	
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column(name="Ex_id")
	private Long Ex_id;
	
	
	
	@Column(name="topicId")
	private int topicId;
	
	@Column(name="exercise_name")
	private String exer_name;
	
	@Column(name="problem_statement")
	private String prob_statement;

    @OneToMany(mappedBy = "exercise")
    private List<Vm_Master> vms;
	
	@Column(name = "hppcode")
	private String hppCode;
	@Column(name = "cid")
	private String cid;
	
	
	
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

	public int getTopicId() {
		return topicId;
	}

	public void setTopicId(int topicId) {
		this.topicId = topicId;
	}
	
	

	public Long getEx_id() {
		return Ex_id;
	}

	public void setEx_id(Long ex_id) {
		Ex_id = ex_id;
	}

	public String getProb_statement() {
		return prob_statement;
	}

	public void setProb_statement(String prob_statement) {
		this.prob_statement = prob_statement;
	}

	public List<Vm_Master> getVms() {
		return vms;
	}

	public void setVms(List<Vm_Master> vms) {
		this.vms = vms;
	}

	public String getExer_name() {
		return exer_name;
	}

	public void setExer_name(String exer_name) {
		this.exer_name = exer_name;
	}

	

    
    
	
}

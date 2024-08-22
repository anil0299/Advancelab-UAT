package com.Advanceelab.cdacelabAdvance.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class AdvanceLabUserVmDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String username;
	private String vmName;
	private String UUID;
	private String ip;
	private Long exerciseId;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getVmName() {
		return vmName;
	}
	public void setVmName(String vmName) {
		this.vmName = vmName;
	}
	public String getUUID() {
		return UUID;
	}
	public void setUUID(String uUID) {
		UUID = uUID;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public Long getExerciseId() {
		return exerciseId;
	}
	public void setExerciseId(Long exerciseId) {
		this.exerciseId = exerciseId;
	}
	@Override
	public String toString() {
		return "AdvanceLabUserVmDetails [id=" + id + ", username=" + username + ", vmName=" + vmName + ", UUID=" + UUID
				+ ", ip=" + ip + ", exerciseId=" + exerciseId + "]";
	}
	
	
}

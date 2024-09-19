package com.Advanceelab.cdacelabAdvance.dto;

public class StudentTrackTime {

	private Long id;
	private String username;
	private String firstLoginTime;
	private String lastLoginTime;
	private String logoutTime;
	private long timeSpentInPortal;
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
	public String getFirstLoginTime() {
		return firstLoginTime;
	}
	public void setFirstLoginTime(String firstLoginTime) {
		this.firstLoginTime = firstLoginTime;
	}
	public String getLastLoginTime() {
		return lastLoginTime;
	}
	public void setLastLoginTime(String lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	public String getLogoutTime() {
		return logoutTime;
	}
	public void setLogoutTime(String logoutTime) {
		this.logoutTime = logoutTime;
	}
	public long getTimeSpentInPortal() {
		return timeSpentInPortal;
	}
	public void setTimeSpentInPortal(long timeSpentInPortal) {
		this.timeSpentInPortal = timeSpentInPortal;
	}
	@Override
	public String toString() {
		return "StudentTrackTime [id=" + id + ", username=" + username + ", firstLoginTime=" + firstLoginTime
				+ ", lastLoginTime=" + lastLoginTime + ", logoutTime=" + logoutTime + ", timeSpentInPortal="
				+ timeSpentInPortal + "]";
	}
}

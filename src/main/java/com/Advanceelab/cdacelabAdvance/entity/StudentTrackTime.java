package com.Advanceelab.cdacelabAdvance.entity;

import java.time.Duration;
import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class StudentTrackTime {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false, unique = true)
	private String username;
	private ZonedDateTime FirstloginTime;
	private ZonedDateTime LastloginTime;
	private ZonedDateTime logoutTime;
	private Duration timeSpentInPortal;
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
	public ZonedDateTime getFirstloginTime() {
		return FirstloginTime;
	}
	public void setFirstloginTime(ZonedDateTime firstloginTime) {
		FirstloginTime = firstloginTime;
	}
	public ZonedDateTime getLastloginTime() {
		return LastloginTime;
	}
	public void setLastloginTime(ZonedDateTime lastloginTime) {
		LastloginTime = lastloginTime;
	}
	public ZonedDateTime getLogoutTime() {
		return logoutTime;
	}
	public void setLogoutTime(ZonedDateTime logoutTime) {
		this.logoutTime = logoutTime;
	}
	public Duration getTimeSpentInPortal() {
		return timeSpentInPortal;
	}
	public void setTimeSpentInPortal(Duration timeSpentInPortal) {
		this.timeSpentInPortal = timeSpentInPortal;
	}
	@Override
	public String toString() {
		return "StudentTrackTime [id=" + id + ", username=" + username + ", FirstloginTime=" + FirstloginTime
				+ ", LastloginTime=" + LastloginTime + ", logoutTime=" + logoutTime + ", timeSpentInPortal="
				+ timeSpentInPortal + "]";
	}
}

package com.Advanceelab.cdacelabAdvance.entity;

import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class EmailCountRP {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Column(unique = true)
	private String emailAddress;
	private int count;
	private ZonedDateTime lastMailSentTime;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public ZonedDateTime getLastMailSentTime() {
		return lastMailSentTime;
	}
	public void setLastMailSentTime(ZonedDateTime lastMailSentTime) {
		this.lastMailSentTime = lastMailSentTime;
	}
	@Override
	public String toString() {
		return "EmailCountRP [id=" + id + ", emailAddress=" + emailAddress + ", count=" + count + ", lastMailSentTime="
				+ lastMailSentTime + "]";
	}
}

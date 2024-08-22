package com.Advanceelab.cdacelabAdvance.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name = "p_VideoWatch")
public class VideoWatch {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	@Column(name = "userid")
	private int userId;
	@Column(name = "videoid")
	private int videoId;
	@Column(name = "videoduration")
	private long videoduration;
	
	@Column(name = "watchtime")
	 private float watchTime;
	@Column(name = "pausetime")
	 private float pauseTime;
	@Column(name = "resumetime")
	 private float resumeTime;
	@Column(name="status")
	private boolean status;
	
	
	
	
	
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public VideoWatch() {
	}
	public  int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getVideoId() {
		return videoId;
	}
	public void setVideoId(int videoId) {
		this.videoId = videoId;
	}

	public float getWatchTime() {
		return watchTime;
	}
	public void setWatchTime(float watchTime) {
		this.watchTime = watchTime;
	}
	public float getPauseTime() {
		return pauseTime;
	}
	public void setPauseTime(float pauseTime) {
		this.pauseTime = pauseTime;
	}
	public float getResumeTime() {
		return resumeTime;
	}
	public void setResumeTime(float resumeTime) {
		this.resumeTime = resumeTime;
	}
	public long getVideoduration() {
		return videoduration;
	}
	public void setVideoduration(long videoduration) {
		this.videoduration = videoduration;
	}
	@Override
	public String toString() {
		return "VideoWatch [id=" + id + ", userId=" + userId + ", videoId=" + videoId + ", videoduration="
				+ videoduration + ", watchTime=" + watchTime + ", pauseTime=" + pauseTime + ", resumeTime=" + resumeTime
				+ ", status=" + status + "]";
	}
	
	
	
	
	
	
	
	
}

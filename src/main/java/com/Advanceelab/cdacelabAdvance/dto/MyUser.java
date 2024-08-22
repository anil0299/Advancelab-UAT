package com.Advanceelab.cdacelabAdvance.dto;

public class MyUser {
	private static String username;
	private static String mypassword;
	private static int batch;
	
	public static String getUsername() {
		return username;
	}
	public static void setUsername(String username) {
		MyUser.username = username;
	}
	public static String getMypassword() {
		return mypassword;
	}
	public static void setMypassword(String mypassword) {
		MyUser.mypassword = mypassword;
	}
	public static int getBatch() {
		return batch;
	}
	public static void setBatch(int batch) {
		MyUser.batch = batch;
	}
	
	
	
	
}

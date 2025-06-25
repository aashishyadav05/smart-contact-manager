package com.smart.helper;

public class Message {

	private String contect;
	private String type;
	
	public String getContect() {
		return contect;
	}
	public void setContect(String contect) {
		this.contect = contect;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
	
	
	public Message(String contect, String type) {
		super();
		this.contect = contect;
		this.type = type;
	}
	
	public Message() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
}

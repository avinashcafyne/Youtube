package com.cafyne.youtube.model;


public class ComplianceWorkFlowComments{
	
	String text;
	String commenterEmpID;
	String commenterEmpName;
//	Date commentCreatDate;
	//TODO need to make it back as Date after implementing gson adapter for date deserialization.
	String commentCreatDate;
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getCommenterEmpID() {
		return commenterEmpID;
	}
	public void setCommenterEmpID(String commenterEmpID) {
		this.commenterEmpID = commenterEmpID;
	}
	public String getCommenterEmpName() {
		return commenterEmpName;
	}
	public void setCommenterEmpName(String commenterEmpName) {
		this.commenterEmpName = commenterEmpName;
	}
	public String getCommentCreatDate() {
		return commentCreatDate;
	}
	public void setCommentCreatDate(String commentCreatDate) {
		this.commentCreatDate = commentCreatDate;
	}
	
	

}

package com.cafyne.youtube.model;

import java.util.List;

public class ComplianceWorkflow {

	String status;
	String state;
	String authorEmpID;
	String authorEmpName;
	Integer ownerEmpID;
	String ownerEmpName;
	String priority;
	String severity;
	String ruleCategoryCSV;
	String ruleDescriptionCSV;
	List<ComplianceWorkFlowComments> commentsHistory;
	
//	Date createdDate;
//	Date lastModifiedDate;
	//TODO need to write Gson adapter for deserialization date. QuickFix convert date as String.
	String createdDate;
	String lastModifiedDate;

	
	
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public String getSeverity() {
		return severity;
	}
	public void setSeverity(String severity) {
		this.severity = severity;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getAuthorEmpID() {
		return authorEmpID;
	}
	public void setAuthorEmpID(String authorEmpID) {
		this.authorEmpID = authorEmpID;
	}
	public String getAuthorEmpName() {
		return authorEmpName;
	}
	public void setAuthorEmpName(String authorEmpName) {
		this.authorEmpName = authorEmpName;
	}
	public Integer getOwnerEmpID() {
		return ownerEmpID;
	}
	public void setOwnerEmpID(Integer ownerEmpID) {
		this.ownerEmpID = ownerEmpID;
	}
	public String getOwnerEmpName() {
		return ownerEmpName;
	}
	public void setOwnerEmpName(String ownerEmpName) {
		this.ownerEmpName = ownerEmpName;
	}
	public List<ComplianceWorkFlowComments> getCommentsHistory() {
		return commentsHistory;
	}
	public void setCommentsHistory(List<ComplianceWorkFlowComments> commentsHistory) {
		this.commentsHistory = commentsHistory;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public String getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setLastModifiedDate(String  lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getRuleCategoryCSV() {
		return ruleCategoryCSV;
	}
	public void setRuleCategoryCSV(String ruleCategoryCSV) {
		this.ruleCategoryCSV = ruleCategoryCSV;
	}
	public String getRuleDescriptionCSV() {
		return ruleDescriptionCSV;
	}
	public void setRuleDescriptionCSV(String ruleDescriptionCSV) {
		this.ruleDescriptionCSV = ruleDescriptionCSV;
	}
}


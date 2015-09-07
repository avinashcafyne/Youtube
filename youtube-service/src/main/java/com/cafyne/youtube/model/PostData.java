package com.cafyne.youtube.model;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;


public class PostData{
	
	String interactionID;
	String profileID;
	int employeeID;
	int employeeGroupId;
	String employeeName;
	String title;
	int companyID;
	String department;
	double nvScoreOfProfile;
	MentionDetails parentConversation;
	List<MentionDetails> mentionDetails = new ArrayList<MentionDetails>();
	DataSiftData postData;
	int sentimentScore;
	HashSet<Violation> violations = new HashSet<Violation>();
	PostAnalyticsData analytics;
	List<ComplianceWorkflow> workflowHistory = new ArrayList<ComplianceWorkflow>();
//	List<NMData> nmDataList;
	String currentActivityStatus;
	String currentActivityState;
	String currentActivityOwner;
	Integer currentActivityOwnerEmpId;
	
	String maxPriority;
	String ruleCategoryCSV;
	String ruleNameCSV;
	String ruleDescriptionCSV;
	boolean isExternalPost;
	int incomingPostToEmpId;
	String incomingPostToEmployeeName;
	int incomingPostToEmployeeGroupId;
	
	Date createdTime;
	Date lastUpdatedTime;
	
	String activityOwner;
	int severityVal;
	
	int retweetCount;
	int replyCount;
	boolean isStimulatorPostEdited;
	
	//isCopy flag to track if this post is copied or not. this will help in restricting updating retweet/reply information.
	boolean isCopy;
	boolean favourited;
	
	public Integer getCurrentActivityOwnerEmpId() {
		return currentActivityOwnerEmpId;
	}

	public void setCurrentActivityOwnerEmpId(Integer currentActivityOwnerEmpId) {
		this.currentActivityOwnerEmpId = currentActivityOwnerEmpId;
	}

	boolean isRuleEnabled;

	public int getSeverityVal() {
		return severityVal;
	}

	public void setSeverityVal(int severityVal) {
		this.severityVal = severityVal;
	}

	public String getActivityOwner() {
		return activityOwner;
	}

	public void setActivityOwner(String activityOwner) {
		this.activityOwner = activityOwner;
	}

	public int getEmployeeGroupId() {
		return employeeGroupId;
	}

	public void setEmployeeGroupId(int employeeGroupId) {
		this.employeeGroupId = employeeGroupId;
	}

	public String getInteractionID() {
		return interactionID;
	}

	public void setInteractionID(String interactionID) {
		this.interactionID = interactionID;
	}

	public String getProfileID() {
		return profileID;
	}

	public void setProfileID(String profileID) {
		this.profileID = profileID;
	}

	public int getEmployeeID() {
		return employeeID;
	}

	public void setEmployeeID(int employeeID) {
		this.employeeID = employeeID;
	}

	public int getCompanyID() {
		return companyID;
	}

	public void setCompanyID(int companyID) {
		this.companyID = companyID;
	}

	public List<MentionDetails> getMentionDetails() {
		return mentionDetails;
	}

	public void setMentionDetails(List<MentionDetails> mentionDetails) {
		this.mentionDetails = mentionDetails;
	}

	public DataSiftData getPostData() {
		return postData;
	}

	public void setPostData(DataSiftData postData) {
		this.postData = postData;
	}

	public int getSentimentScore() {
		return sentimentScore;
	}

	public void setSentimentScore(int sentimentScore) {
		this.sentimentScore = sentimentScore;
	}

	public HashSet<Violation> getViolations() {
		return violations;
	}

	public void setViolations(HashSet<Violation> violations) {
		this.violations = violations;
	}

	public List<ComplianceWorkflow> getWorkflowHistory() {
		return workflowHistory;
	}

	public void setWorkflowHistory(List<ComplianceWorkflow> workflowHistory) {
		this.workflowHistory = workflowHistory;
	}

	public PostAnalyticsData getAnalytics() {
		return analytics;
	}

	public void setAnalytics(PostAnalyticsData analytics) {
		this.analytics = analytics;
	}

	public Date getLastUpdatedTime() {
		return lastUpdatedTime;
	}

	public void setLastUpdatedTime(Date lastUpdatedTime) {
		this.lastUpdatedTime = lastUpdatedTime;
	}

//	public List<NMData> getNmDataList() {
//		return nmDataList;
//	}
//
//	public void setNmDataList(List<NMData> nmDataList) {
//		this.nmDataList = nmDataList;
//	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCurrentActivityStatus() {
		return currentActivityStatus;
	}

	public void setCurrentActivityStatus(String currentActivityStatus) {
		this.currentActivityStatus = currentActivityStatus;
	}

	public String getCurrentActivityState() {
		return currentActivityState;
	}

	public void setCurrentActivityState(String currentActivityState) {
		this.currentActivityState = currentActivityState;
	}

	public String getCurrentActivityOwner() {
		return currentActivityOwner;
	}

	public void setCurrentActivityOwner(String currentActivityOwner) {
		this.currentActivityOwner = currentActivityOwner;
	}

	public String getMaxPriority() {
		return maxPriority;
	}

	public void setMaxPriority(String maxPriority) {
		this.maxPriority = maxPriority;
	}

	public String getRuleCategoryCSV() {
		return ruleCategoryCSV;
	}

	public void setRuleCategoryCSV(String ruleCategoryCSV) {
		this.ruleCategoryCSV = ruleCategoryCSV;
	}

	public String getRuleNameCSV() {
		return ruleNameCSV;
	}

	public void setRuleNameCSV(String ruleNameCSV) {
		this.ruleNameCSV = ruleNameCSV;
	}

	public String getRuleDescriptionCSV() {
		return ruleDescriptionCSV;
	}
	
	public void setRuleDescriptionCSV(String ruleDescriptionCSV) {
		this.ruleDescriptionCSV = ruleDescriptionCSV;
	}

	public double getNvScoreOfProfile() {
		return nvScoreOfProfile;
	}

	public void setNvScoreOfProfile(double nvScoreOfProfile) {
		this.nvScoreOfProfile = nvScoreOfProfile;
	}

	public boolean isRuleEnabled() {
		return isRuleEnabled;
	}

	public void setIsRuleEnabled(boolean isRuleEnabled) {
		this.isRuleEnabled = isRuleEnabled;
	}

	public boolean isExternalPost() {
		return isExternalPost;
	}

	public void setExternalPost(boolean isExternalPost) {
		this.isExternalPost = isExternalPost;
	}

	public int getIncomingPostToEmpId() {
		return incomingPostToEmpId;
	}

	public void setIncomingPostToEmpId(int incomingPostToEmpId) {
		this.incomingPostToEmpId = incomingPostToEmpId;
	}

	public String getIncomingPostToEmployeeName() {
		return incomingPostToEmployeeName;
	}

	public void setIncomingPostToEmployeeName(String incomingPostToEmployeeName) {
		this.incomingPostToEmployeeName = incomingPostToEmployeeName;
	}

	public MentionDetails getParentConversation() {
		return parentConversation;
	}

	public void setParentConversation(MentionDetails parentConversation) {
		this.parentConversation = parentConversation;
	}

	public int getRetweetCount() {
		return retweetCount;
	}

	public void setRetweetCount(int retweetCount) {
		this.retweetCount = retweetCount;
	}

	public int getReplyCount() {
		return replyCount;
	}

	public void setReplyCount(int replyCount) {
		this.replyCount = replyCount;
	}

	public boolean isCopy() {
		return isCopy;
	}

	public void setCopy(boolean isCopy) {
		this.isCopy = isCopy;
	}

	public boolean isStimulatorPostEdited() {
		return isStimulatorPostEdited;
	}

	public void setStimulatorPostEdited(boolean isStimulatorPostEdited) {
		this.isStimulatorPostEdited = isStimulatorPostEdited;
	}

	public int getIncomingPostToEmployeeGroupId() {
		return incomingPostToEmployeeGroupId;
	}

	public void setIncomingPostToEmployeeGroupId(int incomingPostToEmployeeGroupId) {
		this.incomingPostToEmployeeGroupId = incomingPostToEmployeeGroupId;
	}

	public boolean isFavorited() {
		return favourited;
	}

	public void setFavorited(boolean favourited) {
		this.favourited = favourited;
	}
	
	
	
}


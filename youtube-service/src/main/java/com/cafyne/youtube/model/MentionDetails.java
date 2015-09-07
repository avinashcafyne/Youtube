package com.cafyne.youtube.model;

import java.util.Date;

/**
 * 
 * @author nishant.kumar@cafyne.com
 * Details of the post which is comment/reply/like/retweet to the interaction.
 */
public class MentionDetails{
	Date mentionTime;
	String content;
	String profileName;
	String picUrl;
	String channelPostId;
	double sentimentScore;
	double nvScore;
	SocialInteractionType mentionType;
	
	public Date getMentionTime() {
		return mentionTime;
	}
	public void setMentionTime(Date mentionTime) {
		this.mentionTime = mentionTime;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getProfileName() {
		return profileName;
	}
	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}
	public String getPicUrl() {
		return picUrl;
	}
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	public String getChannelPostId() {
		return channelPostId;
	}
	public void setChannelPostId(String channelPostId) {
		this.channelPostId = channelPostId;
	}
	public double getSentimentScore() {
		return sentimentScore;
	}
	public void setSentimentScore(double sentimentScore) {
		this.sentimentScore = sentimentScore;
	}
	public double getNvScore() {
		return nvScore;
	}
	public void setNvScore(double nvScore) {
		this.nvScore = nvScore;
	}
	public SocialInteractionType getMentionType() {
		return mentionType;
	}
	public void setMentionType(SocialInteractionType mentionType) {
		this.mentionType = mentionType;
	}
}

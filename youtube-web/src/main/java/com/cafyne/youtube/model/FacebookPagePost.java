package com.cafyne.youtube.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FacebookPagePost{

	@Expose
	private String content;
	@SerializedName("created_time")
	@Expose
	private String createdTime;
	@Expose
	private FacebookPagePostFrom from;
	@Expose
	private String id;
	@Expose
	private String link;
	@Expose
	private String type;

	/**
	* 
	* @return
	* The content
	*/
	public String getContent() {
	return content;
	}

	/**
	* 
	* @param content
	* The content
	*/
	public void setContent(String content) {
	this.content = content;
	}

	/**
	* 
	* @return
	* The createdTime
	*/
	public String getCreatedTime() {
	return createdTime;
	}

	/**
	* 
	* @param createdTime
	* The created_time
	*/
	public void setCreatedTime(String createdTime) {
	this.createdTime = createdTime;
	}

	/**
	* 
	* @return
	* The from
	*/
	public FacebookPagePostFrom getFrom() {
	return from;
	}

	/**
	* 
	* @param from
	* The from
	*/
	public void setFrom(FacebookPagePostFrom from) {
	this.from = from;
	}

	/**
	* 
	* @return
	* The id
	*/
	public String getId() {
	return id;
	}

	/**
	* 
	* @param id
	* The id
	*/
	public void setId(String id) {
	this.id = id;
	}

	/**
	* 
	* @return
	* The link
	*/
	public String getLink() {
	return link;
	}

	/**
	* 
	* @param link
	* The link
	*/
	public void setLink(String link) {
	this.link = link;
	}

	/**
	* 
	* @return
	* The type
	*/
	public String getType() {
	return type;
	}

	/**
	* 
	* @param type
	* The type
	*/
	public void setType(String type) {
	this.type = type;
	}

}

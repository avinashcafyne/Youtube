package com.cafyne.youtube.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FacebookPage{


	@SerializedName("created_time")
	@Expose
	public String createdTime;
	@Expose
	public FacebookPageFrom from;
	@Expose
	public String id;
	@Expose
	public String message;
	@Expose
	public Page page;
	@SerializedName("status_type")
	@Expose
	public String statusType;
	@Expose
	public String type;
	@SerializedName("updated_time")
	@Expose
	public String updatedTime;
	@SerializedName("post")
	@Expose
	public FacebookPagePost post;

	
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
	public FacebookPageFrom getFrom() {
		return from;
	}

	/**
	 * 
	 * @param from
	 * The from
	 */
	public void setFrom(FacebookPageFrom from) {
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
	 * The message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * 
	 * @param message
	 * The message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * 
	 * @return
	 * The page
	 */
	public Page getPage() {
		return page;
	}

	/**
	 * 
	 * @param page
	 * The page
	 */
	public void setPage(Page page) {
		this.page = page;
	}

	/**
	 * 
	 * @return
	 * The statusType
	 */
	public String getStatusType() {
		return statusType;
	}

	/**
	 * 
	 * @param statusType
	 * The status_type
	 */
	public void setStatusType(String statusType) {
		this.statusType = statusType;
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

	/**
	 * 
	 * @return
	 * The updatedTime
	 */
	public String getUpdatedTime() {
		return updatedTime;
	}

	/**
	 * 
	 * @param updatedTime
	 * The updated_time
	 */
	public void setUpdatedTime(String updatedTime) {
		this.updatedTime = updatedTime;
	}

}

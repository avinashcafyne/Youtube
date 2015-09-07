package com.cafyne.youtube.model;

import com.google.gson.annotations.Expose;

public class FacebookPagePostFrom{

	@Expose
	private String category;
	@Expose
	private String id;
	@Expose
	private String name;

	/**
	* 
	* @return
	* The category
	*/
	public String getCategory() {
	return category;
	}

	/**
	* 
	* @param category
	* The category
	*/
	public void setCategory(String category) {
	this.category = category;
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
	* The name
	*/
	public String getName() {
	return name;
	}

	/**
	* 
	* @param name
	* The name
	*/
	public void setName(String name) {
	this.name = name;
	}

}

package com.cafyne.youtube.model;

import com.google.gson.annotations.Expose;

public class Page{
	
	@Expose
	public String category;
	@Expose
	public String id;
	@Expose
	public String link;
	@Expose
	public String name;
	@Expose
	public String username;

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

	/**
	* 
	* @return
	* The username
	*/
	public String getUsername() {
	return username;
	}

	/**
	* 
	* @param username
	* The username
	*/
	public void setUsername(String username) {
	this.username = username;
	}
}

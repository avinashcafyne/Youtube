package com.cafyne.youtube.model;

import java.util.List;

import com.google.api.services.youtube.model.SearchResult;

public class GnipYouTube {

	String data_collector_id;
	String refreshURL;
	String publisher;
	String data_collector_name;
	String endpoint;
	List<SearchResult> entry;
	public String getData_collector_id() {
		return data_collector_id;
	}
	public void setData_collector_id(String data_collector_id) {
		this.data_collector_id = data_collector_id;
	}
	public String getRefreshURL() {
		return refreshURL;
	}
	public void setRefreshURL(String refreshURL) {
		this.refreshURL = refreshURL;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	public String getData_collector_name() {
		return data_collector_name;
	}
	public void setData_collector_name(String data_collector_name) {
		this.data_collector_name = data_collector_name;
	}
	public String getEndpoint() {
		return endpoint;
	}
	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}
	public List<SearchResult> getEntry() {
		return entry;
	}
	public void setEntry(List<SearchResult> entry) {
		this.entry = entry;
	}
	
}

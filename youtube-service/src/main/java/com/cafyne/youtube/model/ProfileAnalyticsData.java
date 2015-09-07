package com.cafyne.youtube.model;

import java.io.Serializable;

public class ProfileAnalyticsData implements Serializable{

	private static final long serialVersionUID = 2003452533004500489L;
	double socialEquity;
	double impactIndex;
	double complianceScore;
	double sentiment;
	double nvScore;
	double socialEquityPercentile;
	double impactIndexPercentile;
	
	public double getSocialEquity() {
		return socialEquity;
	}
	public void setSocialEquity(double socialEquity) {
		this.socialEquity = socialEquity;
	}
	public double getImpactIndex() {
		return impactIndex;
	}
	public void setImpactIndex(double impactIndex) {
		this.impactIndex = impactIndex;
	}
	public double getComplianceScore() {
		return complianceScore;
	}
	public void setComplianceScore(double complianceScore) {
		this.complianceScore = complianceScore;
	}
	public double getSentiment() {
		return sentiment;
	}
	public void setSentiment(double sentiment) {
		this.sentiment = sentiment;
	}
	public double getNvScore() {
		return nvScore;
	}
	public void setNvScore(double nvScore) {
		this.nvScore = nvScore;
	}
	public double getSocialEquityPercentile() {
		return socialEquityPercentile;
	}
	public void setSocialEquityPercentile(double socialEquityPercentile) {
		this.socialEquityPercentile = socialEquityPercentile;
	}
	public double getImpactIndexPercentile() {
		return impactIndexPercentile;
	}
	public void setImpactIndexPercentile(double impactIndexPercentile) {
		this.impactIndexPercentile = impactIndexPercentile;
	}
	
}

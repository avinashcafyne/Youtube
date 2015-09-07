package com.cafyne.youtube.model;

import java.io.Serializable;

public class RuleElement implements Serializable{

	private static final long serialVersionUID = -706236552569792071L;
	private String ruleElementId;
	private String ruleElementName;
	private String alertWords;
	private Integer ruleElementType;

	public String getRuleElementId() {
		return ruleElementId;
	}

	public void setRuleElementId(String ruleElementId) {
		this.ruleElementId = ruleElementId;
	}

	public String getRuleElementName() {
		return ruleElementName;
	}

	public void setRuleElementName(String ruleElementName) {
		this.ruleElementName = ruleElementName;
	}

	public String getAlertWords() {
		return alertWords;
	}

	public void setAlertWords(String alertWords) {
		this.alertWords = alertWords;
	}

	public Integer getRuleElementType() {
		return ruleElementType;
	}

	public void setRuleElementType(Integer ruleElementType) {
		this.ruleElementType = ruleElementType;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return ruleElementName;
	}

}

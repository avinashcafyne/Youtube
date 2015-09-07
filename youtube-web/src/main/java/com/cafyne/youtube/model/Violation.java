package com.cafyne.youtube.model;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;



public class Violation implements Comparable<Violation>{

	private String message;

	private String stream;	

	private double impact;
	
	private int sentiment;		

	private String url;
	
	private String priority;

	private String tigger_time;
	
	private String biography;
	
	private String entity;
	
	private List<String> brands;
	
	private List<String> companyIds;
	
	private List<String> companyBrands;
	
	private List<String> slurs;
	
	private List<String> links;
	
	private List<String> ffiecIds;
	
	private List<String> insultCategory;
	
	private List<String> slurType;
	
	private List<String> explicitCategory;
	
	private List<String> messageList;
	
	private List<String> bioList;
	
	private List<String> officialList;
	
	private List<String> financialInfo;
	
	private List<String> confMention;
	
	private List<String> patentMention;
	
	private List<String> sexistLang;
	
	private List<String> racistLang;
	
	private List<String> marketingPrint;
	
	private List<String> disclaimer;
	
	private List<String> discrim;
	
	private List<String> identifyWCompany;
	
	private Set<RuleElement> applicableRuleElements;
	
	private Map<String, String> applicableRulesMap;
	
	private Map<String, Set<String>> applicableRuleElementPolicyMap;
	
	private String policyName;	//policy comma seperated
	
	private String violation;	// rule elements comma seperated
	
	private String category; //rules comma seperated
	
	private Properties droolsProperty;
	
	private List<String> appDiscl;
	
	public List<String> getIdentifyWCompany() {
		return identifyWCompany;
	}

	public void setIdentifyWCompany(List<String> identifyWCompany) {
		this.identifyWCompany = identifyWCompany;
	}

	public List<String> getDisclaimer() {
		return disclaimer;
	}

	public void setDisclaimer(List<String> disclaimer) {
		this.disclaimer = disclaimer;
	}

	public List<String> getDiscrim() {
		return discrim;
	}

	public void setDiscrim(List<String> discrim) {
		this.discrim = discrim;
	}

	public List<String> getMarketingPrint() {
		return marketingPrint;
	}

	public void setMarketingPrint(List<String> marketingPrint) {
		this.marketingPrint = marketingPrint;
	}

	public List<String> getRacistLang() {
		return racistLang;
	}

	public void setRacistLang(List<String> racistLang) {
		this.racistLang = racistLang;
	}

	public Set<RuleElement> getApplicableRuleElements() {
		return applicableRuleElements;
	}

	public void setApplicableRuleElements(Set<RuleElement> applicableRuleElements) {
		this.applicableRuleElements = applicableRuleElements;
	}

	public Map<String, String> getApplicableRulesMap() {
		return applicableRulesMap;
	}

	public void setApplicableRulesMap(Map<String, String> applicableRulesMap) {
		this.applicableRulesMap = applicableRulesMap;
	}

	public Map<String, Set<String>> getApplicableRuleElementPolicyMap() {
		return applicableRuleElementPolicyMap;
	}

	public void setApplicableRuleElementPolicyMap(
			Map<String, Set<String>> applicableRuleElementPolicyMap) {
		this.applicableRuleElementPolicyMap = applicableRuleElementPolicyMap;
	}

	public List<String> getSexistLang() {
		return sexistLang;
	}

	public void setSexistLang(List<String> sexistLang) {
		this.sexistLang = sexistLang;
	}

	public List<String> getPatentMention() {
		return patentMention;
	}

	public void setPatentMention(List<String> patentMention) {
		this.patentMention = patentMention;
	}

	public List<String> getConfMention() {
		return confMention;
	}

	public void setConfMention(List<String> confMention) {
		this.confMention = confMention;
	}

	public List<String> getFinancialInfo() {
		return financialInfo;
	}

	public void setFinancialInfo(List<String> financialInfo) {
		this.financialInfo = financialInfo;
	}

	public List<String> getAppDiscl() {
		return appDiscl;
	}

	public void setAppDiscl(List<String> appDiscl) {
		this.appDiscl = appDiscl;
	}

	public List<String> getOfficialList() {
		return officialList;
	}

	public void setOfficialList(List<String> officialList) {
		this.officialList = officialList;
	}

	public List<String> getBioList() {
		return bioList;
	}

	public void setBioList(List<String> bioList) {
		this.bioList = bioList;
	}

	public Properties getDroolsProperty() {
		return droolsProperty;
	}

	public void setDroolsProperty(Properties droolsProperty) {
		this.droolsProperty = droolsProperty;
	}

	public List<String> getMessageList() {
		return messageList;
	}

	public void setMessageList(List<String> messageList) {
		this.messageList = messageList;
	}

	public List<String> getInsultCategory() {
		return insultCategory;
	}

	public void setInsultCategory(List<String> insultCategory) {
		this.insultCategory = insultCategory;
	}

	public List<String> getSlurType() {
		return slurType;
	}

	public void setSlurType(List<String> slurType) {
		this.slurType = slurType;
	}

	public List<String> getExplicitCategory() {
		return explicitCategory;
	}

	public void setExplicitCategory(List<String> explicitCategory) {
		this.explicitCategory = explicitCategory;
	}

	public String getPolicyName() {
		return policyName;
	}

	public void setPolicyName(String policyName) {
		this.policyName = policyName;
	}

	public int getSentiment() {
		return sentiment;
	}

	public void setSentiment(int sentiment) {
		this.sentiment = sentiment;
	}
	
	public List<String> getFfiecIds() {
		return ffiecIds;
	}

	public void setFfiecIds(List<String> ffiecIds) {
		this.ffiecIds = ffiecIds;
	}

	public List<String> getLinks() {
		return links;
	}

	public void setLinks(List<String> links) {
		this.links = links;
	}

	public List<String> getSlurs() {
		return slurs;
	}

	public void setSlurs(List<String> slurs) {
		this.slurs = slurs;
	}

	public List<String> getCompanyBrands() {
		return companyBrands;
	}

	public void setCompanyBrands(List<String> companyBrands) {
		this.companyBrands = companyBrands;
	}

	public List<String> getCompanyIds() {
		return companyIds;
	}

	public void setCompanyIds(List<String> companyIds) {
		this.companyIds = companyIds;
	}

	public List<String> getStakeholders() {
		return stakeholders;
	}

	public void setStakeholders(List<String> stakeholders) {
		this.stakeholders = stakeholders;
	}


	private List<String> stakeholders;
	
	public List<String> getBrands() {
		return brands;
	}

	public void setBrands(List<String> brands) {
		this.brands = brands;
	}

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}


	public String getBiography() {
		return biography;
	}

	public void setBiography(String biography) {
		this.biography = biography;
	}

	public String getTigger_time() {
		return tigger_time;
	}

	public void setTigger_time(String tigger_time) {
		this.tigger_time = tigger_time;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public double getImpact() {
		return impact;
	}

	public void setImpact(double impact) {
		this.impact = impact;
	}
	
	public String getStream() {
		return stream;
	}
	public void setStream(String stream) {
		this.stream = stream;
	}
	public String getRuleId() {
		return ruleId;
	}
	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}

	private String ruleId;
	private String ruleDescription;
	public String getRuleDescription() {
		return ruleDescription;
	}
	public void setRuleDescription(String ruleDescription) {
		this.ruleDescription = ruleDescription;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	public String getViolation() {
		return violation;
	}
	public void setViolation(String violation) {
		this.violation = violation;		
	}
	
	public Violation(){
		super();
	}


	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Violation))
			return false;
		if (obj == this)
			return true;
		return  this.ruleId.equals(((Violation) obj).ruleId);
	}
	public int compareTo(Violation o) {

		return this.violation.equalsIgnoreCase(o.message)?0:-1;
	}
}

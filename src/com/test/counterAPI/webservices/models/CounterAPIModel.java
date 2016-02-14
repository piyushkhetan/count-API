package com.test.counterAPI.webservices.models;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAutoDetect;

@JsonAutoDetect
public class CounterAPIModel implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private List<String> searchText;
	private Map<String, Integer> counts;
	private String paragraphText;
	/**
	 * @return the searchText
	 */
	public List<String> getSearchText() {
		return searchText;
	}
	/**
	 * @param searchText the searchText to set
	 */
	public void setSearchText(List<String> searchText) {
		this.searchText = searchText;
	}
	/**
	 * @return the counts
	 */
	public Map<String, Integer> getCounts() {
		return counts;
	}
	/**
	 * @param counts the counts to set
	 */
	public void setCounts(Map<String, Integer> counts) {
		this.counts = counts;
	}
	/**
	 * @return the paragraphText
	 */
	public String getParagraphText() {
		return paragraphText;
	}
	/**
	 * @param paragraphText the paragraphText to set
	 */
	public void setParagraphText(String paragraphText) {
		this.paragraphText = paragraphText;
	}
	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
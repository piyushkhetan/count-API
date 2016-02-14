package com.test.counterAPI.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.codec.Base64;

public class APIUtil {

	private static final Logger LOG = Logger.getLogger(APIUtil.class);

	/**
	 * Returns the map with given input text as key and total occurrence count
	 * as value.
	 * 
	 * @param searchText
	 * @return
	 */
	public static Map<String, Integer> getTextCounts(List<String> searchText) {
		Map<String, Integer> countResultMap = new HashMap<String, Integer>();
		try{
			String paragraphText = Constants.PARAGRAPH_TEXT;
			if (null != searchText) {
				for (String text : searchText) {
					countResultMap.put(text, StringUtils.countMatches(paragraphText, text));
				}
			}
		}catch(Exception exception){
			LOG.info("Error occurred in getTextCounts method\n"+exception);			
		}
		return countResultMap;
	}

	/**
	 * Returns the top occurrences of text as given input.
	 * 
	 * @param top
	 */
	public static List<String[]> getMaxOccurrence(int top) {
		List<String[]> result = new ArrayList<String[]>();
		try{
			Map<String, Integer> countMap = new HashMap<String, Integer>();
			setCounterMap(countMap);
			List<Entry<String, Integer>> list = sortCounterMap(countMap);
			generateCSVData(list,result,top);
		}catch(Exception exception){
			LOG.info("Error occurred in getMaxOccurrence method\n"+exception);
		}
		return result;
	}

	/**
	 * Generates the CSV data for required top words. 
	 * @param list
	 * @param result
	 */
	private static void generateCSVData(List<Entry<String, Integer>> list, List<String[]> result,int top) {
		try{
			for(int i=0;i<(list.size()>top?top:list.size());i++){
				Entry<String, Integer> entry = list.get(i);
				String[] str = new String[2];
				str[0] = entry.getKey();
				str[1] = String.valueOf(entry.getValue());
				result.add(str);
			}
		}catch(Exception exception){
			LOG.info("Error in generateCSVData method\n"+exception);			
		}
		
	}

	/**
	 * Sorts the input map by value in descending order.
	 * @param countMap
	 * @return
	 */
	private static List<Entry<String, Integer>> sortCounterMap(Map<String, Integer> countMap) {
		List<Entry<String, Integer>> list = null;
		try{
			Set<Entry<String, Integer>> set = countMap.entrySet();
			list = new ArrayList<Entry<String, Integer>>(set);
			Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
				public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
					return (o2.getValue()).compareTo(o1.getValue());
				}
			});
		}catch(Exception exception){
			LOG.info("Error in sortCounterMap method\n"+exception);
		}
		return list;
	}

	/**
	 * Reads/Splits each word in paragraph and sets counter for each word in map.
	 * @param countMap
	 */
	private static void setCounterMap(Map<String, Integer> countMap) {
		String text = Constants.PARAGRAPH_TEXT;
		try {
			String[] textArray = text.split(Constants.SPLIT_REGEX);
			for (String word : textArray) {
				if (countMap.containsKey(word)) {
					int counter = countMap.get(word) + 1;
					countMap.put(word, counter);
				} else if(!Constants.BLANK_STRING.equals(word.trim())){
					countMap.put(word, 1);
				}
			}
		} catch (Exception exception) {
			LOG.info("Error in setCounterMap method\n"+exception);
		}
	}
	
	/**
	 * Decode input header with authorization. It will contain username and password.
	 * @param authorization
	 * @return
	 */
	public static String[] decodeHeader(String authorization) {
		try {
			byte[] decoded = Base64.decode(authorization.substring(0).getBytes("UTF-8"));
			String credentials = new String(decoded);
			return credentials.split(Constants.AUTHORIZATION_DELIMETER);
		} catch (Exception e) {
			LOG.info("Error in decodeHeader method\n", e);
			throw new BadCredentialsException("Given User is not authorized.");
		}
	}
	
}

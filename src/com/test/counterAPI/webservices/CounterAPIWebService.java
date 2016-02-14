package com.test.counterAPI.webservices;

import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.test.counterAPI.Util.APIUtil;
import com.test.counterAPI.Util.Constants;
import com.test.counterAPI.webservices.models.CounterAPIModel;

import au.com.bytecode.opencsv.CSVWriter;

@Controller
public class CounterAPIWebService {
	private static final Logger LOG = Logger.getLogger(CounterAPIWebService.class);
	/**
	 * Searches for the required text and returns with total occurrences of word.
	 * @param counterAPIModel
	 * @return
	 */
	@RequestMapping(value = "/search", method = RequestMethod.POST, headers = "Accept=application/json", produces = "application/json;charset=utf-8", consumes = "application/json")
	public @ResponseBody String searchText( @RequestBody CounterAPIModel counterAPIModel){
		Gson gson = new Gson();
		try {
			Map<String, Integer> countResultMap = APIUtil.getTextCounts(counterAPIModel.getSearchText());
			counterAPIModel.setSearchText(null);
			counterAPIModel.setCounts(countResultMap);			
		} catch (Exception exception) {
			LOG.info("Something wrong has happened",exception);
		}
		return gson.toJson(counterAPIModel);
	} 

	/**
	 * Returns the top words with total number of occurrences. Use input as id.
	 * @param id
	 * @param response
	 */
	@RequestMapping(value = "/top/{count}", method = RequestMethod.POST, headers = "Accept=application/json", produces = "text/csv", consumes = "application/json")
	public @ResponseBody void topText(@PathVariable int count, HttpServletResponse response){
		try {
			List<String[]> result = APIUtil.getMaxOccurrence(count);	
			response.setHeader("Content-Disposition", "attachment; filename=counter_result.csv");
            response.setContentType("text/csv");
            OutputStreamWriter osw = new OutputStreamWriter(response.getOutputStream(), "UTF-8");
            CSVWriter csvWriter = new CSVWriter(osw, Constants.CSV_DELIMETER);
            csvWriter.writeAll(result);
            csvWriter.flush();
            csvWriter.close();
		} catch (Exception exception) {
			LOG.info("Something wrong has happened",exception);
		}
	} 
	
	/**
	 * Returns the paragraph text.
	 * @return
	 */
	@RequestMapping(value = "/text", method = RequestMethod.POST, headers = "Accept=application/json", produces = "application/json;charset=utf-8", consumes = "application/json")
	public @ResponseBody String text(){
		Gson gson = new Gson();
		CounterAPIModel counterAPIModel = new CounterAPIModel();
		try {			
			counterAPIModel.setParagraphText(Constants.PARAGRAPH_TEXT);
		} catch (Exception exception) {
			LOG.info("Something wrong has happened",exception);
		}
		return gson.toJson(counterAPIModel);
	} 
}

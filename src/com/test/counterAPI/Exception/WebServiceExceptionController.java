package com.test.counterAPI.Exception;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.google.gson.Gson;
import com.test.counterAPI.Util.APIUtil;
import com.test.counterAPI.webservices.models.APISecurityModel;

 
@ControllerAdvice
public class WebServiceExceptionController {
	Gson gson = new Gson();
	private static final Logger LOG = Logger.getLogger(APIUtil.class);

	/**
	 * Handle all exception.
	 *
	 * @param 
	 * @return the string
	 */
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
	@ResponseBody
	public String handleAllException(Exception exception) {
		LOG.info("Error occurred in application\n"+exception);
		return gson.toJson("Something wrong has happened. Please check the log");
	}
 
/**
 * Handle web services authentication exception.
 *
 * @param request
 * @param response
 * @param authException the auth exception
 * @throws IOException Signals that an I/O exception has occurred.
 */
public static void handleWebServicesAuthenticationException(HttpServletRequest request,
		HttpServletResponse response,AuthenticationException authException) 
          throws IOException{
	
	APISecurityModel apiSecurity = new APISecurityModel();
	apiSecurity.setStatus("false");
	apiSecurity.setMessage(authException.getMessage());	
	Gson gson=new Gson();
	
	response.setContentType("application/json;charset=UTF-8");
	response.setStatus(HttpServletResponse.SC_OK);
    PrintWriter writer = response.getWriter();
    writer.println(gson.toJson(apiSecurity));
}
}
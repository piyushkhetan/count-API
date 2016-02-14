package com.test.counterAPI.restSecurity;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.test.counterAPI.Exception.WebServiceExceptionController;

/**
 * Authentication entry point for REST services
 */
public final class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

	private static final Logger LOG = Logger.getLogger(RestAuthenticationEntryPoint.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.security.web.AuthenticationEntryPoint#commence(javax.
	 * servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
	 * org.springframework.security.core.AuthenticationException)
	 */
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException {
		LOG.info("in entry");
		WebServiceExceptionController.handleWebServicesAuthenticationException(request, response,
				authException);
	}
}
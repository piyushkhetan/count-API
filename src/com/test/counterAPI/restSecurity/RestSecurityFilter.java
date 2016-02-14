package com.test.counterAPI.restSecurity;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.web.filter.GenericFilterBean;

import com.test.counterAPI.Exception.WebServiceExceptionController;
import com.test.counterAPI.Util.APIUtil;
import com.test.counterAPI.Util.Constants;

/**
 * Authentication filter for REST services
 */
public class RestSecurityFilter extends GenericFilterBean {

	private static final Logger LOG = Logger.getLogger(RestSecurityFilter.class);
	private AuthenticationManager authenticationManager;
	private AuthenticationEntryPoint authenticationEntryPoint;

	public RestSecurityFilter(AuthenticationManager authenticationManager) {
		this(authenticationManager, new BasicAuthenticationEntryPoint());
		((BasicAuthenticationEntryPoint) authenticationEntryPoint).setRealmName("counter-api");
	}

	/**
	 * Instantiates a new rest security filter.
	 *
	 * @param authenticationManager
	 * @param authenticationEntryPoint
	 */
	public RestSecurityFilter(AuthenticationManager authenticationManager,
			AuthenticationEntryPoint authenticationEntryPoint) {
		this.authenticationManager = authenticationManager;
		this.authenticationEntryPoint = authenticationEntryPoint;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
	 * javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;

		String authorization = request.getHeader("Authorization");

		LOG.info("Authorization:" + authorization);

		if (authorization == null) {
			chain.doFilter(request, response);
			return;
		}

		try {

			String[] credentials = APIUtil.decodeHeader(authorization);
			assert credentials.length == 2;

			if (credentials.length == 2 && 
					Constants.USER_NAME.equals(credentials[0]) && 
					Constants.PASSWORD.equals(credentials[1])) {
				Authentication authentication = new UsernamePasswordAuthenticationToken(credentials[0], credentials[1]);

				LOG.info("In Try Filter after credential check");

				Authentication successfulAuthentication = authenticationManager.authenticate(authentication);
				SecurityContextHolder.getContext().setAuthentication(successfulAuthentication);

				chain.doFilter(request, response);
			} else {
				throw new BadCredentialsException("Given User is not authorized.");
			}
		} catch (AuthenticationException authenticationException) {
			LOG.info("Given User is not authorized.");
			SecurityContextHolder.clearContext();
			WebServiceExceptionController.handleWebServicesAuthenticationException(request, response,
					authenticationException);
		}
	}

}
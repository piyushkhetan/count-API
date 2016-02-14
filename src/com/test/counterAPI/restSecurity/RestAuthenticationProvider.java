package com.test.counterAPI.restSecurity;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class RestAuthenticationProvider implements AuthenticationProvider {

	private static final Logger LOG = Logger.getLogger(RestAuthenticationProvider.class);

	/*
	 * (non-Javadoc)
	 */
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		Authentication auth=null; 
		try {
			UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
			String key = (String) token.getPrincipal();
			String credentials = (String) token.getCredentials();
			auth=getAuthenticatedUser(key, credentials);
		} catch (Exception e) {
			LOG.info("Error occurred in authenticate method",e);			
		}
		LOG.info("after rest authentication try catch");

		return auth;
	}

	/**
	 * Gets the authenticated user.
	 *
	 * @param key
	 * @param credentials
	 * @return the authenticated user
	 */
	private Authentication getAuthenticatedUser(String key, String credentials) {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

		return new UsernamePasswordAuthenticationToken(key, credentials, authorities);
	}

	/**
	 * Supports.
	 *
	 * @param authentication
	 * @return true, if successful
	 */
	@Override
	public boolean supports(Class<? extends Object> authentication) {

		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}

}

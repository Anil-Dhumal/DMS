package com.dms.securityInfo;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

//Create a utility class to fetch the currently logged-in user from the JWT: forlog-Audit

public class SecurityUtil {
	
	 public static String getCurrentUsername() {
	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

	        if (authentication == null || !authentication.isAuthenticated()
	            || authentication instanceof AnonymousAuthenticationToken) {
	            return "Anonymous";
	        }

	        return authentication.getName();
	    }

}

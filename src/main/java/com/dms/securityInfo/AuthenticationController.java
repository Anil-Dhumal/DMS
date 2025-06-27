package com.dms.securityInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping
public class AuthenticationController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;

	@Autowired
	private JwtUtils jwtUtils;

	@PostMapping("/authenticate")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthRequest authRequest) {
		System.out.println(" LOGIN ATTEMPT..:");
		System.out.println("Username: " + authRequest.getUsername());
		System.out.println("Password: " + authRequest.getPassword());

		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
		} catch (BadCredentialsException e) {
			return ResponseEntity.status(401).body(" Invalid username or password");
		}

		final UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(authRequest.getUsername());
		final String jwt = jwtUtils.generateToken(userDetails);

		return ResponseEntity.ok(new AuthResponse(jwt));
	}
}

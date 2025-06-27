package com.dms.securityInfo;

import java.util.Collections;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.dms.model.User;
import com.dms.repository.UserRepository;



@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	 // You can inject UserRepository here if loading from DB
	@Autowired
	private UserRepository userRepository;
	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user = userRepository.findByUserName(username)
				    .orElseThrow(() -> new UsernameNotFoundException("Hey ..USERNAME NOT FOUND.."));
		
		return new org.springframework.security.core.userdetails.User(
				user.getUserName(),
				user.getPassword(),
				Collections.singleton(new SimpleGrantedAuthority("ROLE_" +user.getRole().name())));
		
		
		//  Hardcoded user for learning/testing
		
	/*	System.out.println("Loading User : " + username);
		
        if ("admin".equals(username)) {
            return new User(
                    "admin",
                    "{noop}password", //  No encoding, use {noop} for plain text
                    new ArrayList<>() //  Empty roles/authorities for now
            );
        } else {
            throw new UsernameNotFoundException("User not found....: " + username);
        }     */
    }          
}

package com.dms;

import com.dms.controller.DocumentController;
import com.dms.model.Role;
import com.dms.model.User;
import com.dms.repository.UserRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication(scanBasePackages = "com.dms")
@ComponentScan
public class DocumentManagementSystemApplication {

    private final DocumentController documentController;

    DocumentManagementSystemApplication(DocumentController documentController) {
        this.documentController = documentController;
    }

	public static void main(String[] args) {
		String rawPassword = "password";
		String encodePassword =new BCryptPasswordEncoder().encode(rawPassword);
		System.out.println(encodePassword);
		
		SpringApplication.run(DocumentManagementSystemApplication.class, args);
	}
	
	
	@Bean
	public CommandLineRunner insertDefaultUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {
	    return args -> {
	        if (userRepository.findByUserName("admin").isEmpty()) {
	            User admin = new User();
	            admin.setUserName("admin");
	            admin.setPassword(passwordEncoder.encode("password"));
	            admin.setRole(Role.ADMIN);
	            userRepository.save(admin);
	        }

	        if (userRepository.findByUserName("user").isEmpty()) {
	            User user = new User();
	            user.setUserName("user");
	            user.setPassword(passwordEncoder.encode("password"));
	            user.setRole(Role.USER);
	            userRepository.save(user);
	        }
	    };
	}


}

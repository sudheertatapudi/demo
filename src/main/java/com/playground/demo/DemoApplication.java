package com.playground.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
//import org.springframework.security.crypto.password.PasswordEncoder;

import com.playground.demo.model.DemoUser;
import com.playground.demo.model.DemoRole;
import com.playground.demo.repository.UserRepository;
import com.playground.demo.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	CommandLineRunner init(UserRepository userRepository, 
						  RoleRepository roleRepository
						  /*PasswordEncoder passwordEncoder*/) {
		return args -> {
			// Create roles if they don't exist
			if (roleRepository.count() == 0) {
				roleRepository.save(new DemoRole("ROLE_USER"));
				roleRepository.save(new DemoRole("ROLE_MANAGER"));
				roleRepository.save(new DemoRole("ROLE_ADMIN"));
				log.info("Default roles created successfully");
			} else {
				log.info("Default roles already exist");
			}

			// Create users if they don't exist
			if (userRepository.count() == 0) {
				// Create a regular user
				DemoUser user = new DemoUser();
				user.setUsername("sudheer");
				//user.setPassword(passwordEncoder.encode("sudheer123"));
				user.setPassword("sudheer123");
				user.setEmail("sudheer@gmail.com");
				user.addRole(roleRepository.findByName("ROLE_USER"));
				userRepository.save(user);
				log.info("Regular user 'sudheer' created successfully");

				// Create an admin user
				DemoUser admin = new DemoUser();
				admin.setUsername("admin");
				//admin.setPassword(passwordEncoder.encode("admin123"));
				admin.setPassword("admin123");
				admin.setEmail("admin@example.com");
				admin.addRole(roleRepository.findByName("ROLE_ADMIN"));
				admin.addRole(roleRepository.findByName("ROLE_USER"));
				userRepository.save(admin);
				log.info("Admin user created successfully");
			} else {
				log.info("Users already exist in the database");
			}
		};
	}
}

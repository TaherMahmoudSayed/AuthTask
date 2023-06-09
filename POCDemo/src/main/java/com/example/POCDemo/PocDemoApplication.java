package com.example.POCDemo;

import com.example.POCDemo.domain.Role;
import com.example.POCDemo.domain.User;
import com.example.POCDemo.service.userService.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class PocDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(PocDemoApplication.class, args);
	}
	@Bean
	CommandLineRunner run(UserService userService,PasswordEncoder passwordEncoder) {
		return args -> {
			// creating roles
			userService.saveRole(Role.builder().role("Role_User").build());
			userService.saveRole(Role.builder().role("Role_Manager").build());
			userService.saveRole(Role.builder().role("Role_Admin").build());
			userService.saveRole(Role.builder().role("Role_Super_Admin").build());

			// creating Users
			userService.saveUser(User.builder().userName("TaherTito")
					.firstName("taher").lastName("tito").password(passwordEncoder.encode("123")).build());
			userService.saveUser(User.builder().userName("TaherTito1")
					.firstName("taher").lastName("tito").password(passwordEncoder.encode("123")).build());
			userService.saveUser(User.builder().userName("TaherTito2")
					.firstName("taher").lastName("tito").password(passwordEncoder.encode("123")).build());
			userService.saveUser(User.builder().userName("TaherTito3")
					.firstName("taher").lastName("tito").password(passwordEncoder.encode("123")).build());
			userService.saveUser(User.builder().userName("TaherTito4")
					.firstName("taher").lastName("tito").password(passwordEncoder.encode("123")).build());

			// adding roles to users
			userService.attachRoleToUser("Role_User","TaherTito1");
			userService.attachRoleToUser("Role_Manager","TaherTito2");
			userService.attachRoleToUser("Role_Admin","TaherTito2");
			userService.attachRoleToUser("Role_User","TaherTito3");
			userService.attachRoleToUser("Role_Manager","TaherTito4");
			userService.attachRoleToUser("Role_User","TaherTito2");
			userService.attachRoleToUser("Role_Super_Admin","TaherTito");


		};
	}

}

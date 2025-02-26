package com.lcwd.electronic.store;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.lcwd.electronic.store.entities.Role;
import com.lcwd.electronic.store.exception.BadRequestException;
import com.lcwd.electronic.store.repositories.RoleRepository;

@SpringBootApplication
public class ElectronicStoreApplication implements CommandLineRunner {

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	RoleRepository roleRepository;

	@Value("${normal.role.id}")
	private String normalUserId;

	@Value("${admin.role.id}")
	private String adminId;

	public static void main(String[] args) {
		SpringApplication.run(ElectronicStoreApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		try {
			Role roleUser = Role.builder().roleId(normalUserId).roleName("ROLE_USER").build();
			Role roleAdmin = Role.builder().roleId(adminId).roleName("ROLE_ADMIN").build();
			roleRepository.save(roleUser);
			roleRepository.save(roleAdmin);
		} catch (Exception e) {
			throw new BadRequestException("Roles Not Save Properly !!");
		}

	}
}

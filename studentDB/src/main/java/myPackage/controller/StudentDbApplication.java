package myPackage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import myPackage.entity.Student;
import myPackage.repository.StudentRepository;

@SpringBootApplication
@EntityScan("myPackage.entity") 
@ComponentScan({"myPackage"})
@EnableJpaRepositories("myPackage.repository")

public class StudentDbApplication {
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private StudentRepository repo;

	public static void main(String[] args) {
		SpringApplication.run(StudentDbApplication.class, args);
	}

	@Bean
	public void initDatabase() {
		Student admin = new Student();
		admin.setEmail("admin");
		admin.setPassword(passwordEncoder.encode("pass"));
		admin.setRole("ADMIN");
		admin.setEnabled(true);
		repo.save(admin);
	}
}

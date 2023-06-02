package studentDB;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;

import studentDB.entity.Student;
import studentDB.repository.StudentRepository;

@SpringBootApplication
@EntityScan("studentDB.entity") 
@ComponentScan({"studentDB"})
@EnableJpaRepositories("studentDB.repository")
@EnableScheduling
public class StudentDbApplication {
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
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

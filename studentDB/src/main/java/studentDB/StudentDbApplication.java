package studentDB;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("entity") 
public class StudentDbApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudentDbApplication.class, args);
	}

}

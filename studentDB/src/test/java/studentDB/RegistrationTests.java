package studentDB;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import myPackage.repository.StudentRepository;
import myPackage.controller.StudentDbApplication;
import myPackage.entity.Corso;
import myPackage.entity.RegistrationData;
import myPackage.entity.Student;

@Import({TestConfig.class})
@SpringBootTest(classes = StudentDbApplication.class)
@AutoConfigureMockMvc
public class RegistrationTests {
    
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private StudentRepository repo;
	
	@Test
	public void getRegistrationForm() throws Exception {
		
		String expectedResponse = util.readHtml("classpath:templates/register.html");
		mockMvc.perform(get("/register"))
				.andExpect(status().isOk())
				.andExpect(content().string(expectedResponse));
	}

	@Test
	public void register_0() throws Exception {

		// Send registration data
		RegistrationData data = new RegistrationData(
				"Mario","Rossi","mariorossi@gmail.com","Password123@","Password123@",
				Corso.Associate.toString());
		
		String expectedResponse = util.readHtml("classpath:templates/RegistrationSuccess.html");
		mockMvc.perform(post("/register")
					.flashAttr("registrationData", data))
					.andExpect(status().isOk())
					.andExpect(content().string(expectedResponse));
	    
		Student student = repo.findByEmail("mariorossi@gmail.com");		
		assert(student != null && util.checkEquals(student, data));  
				
		// Verify
		String verificationCode = student.getVerificationCode();
		assert(verificationCode != null);
		
		expectedResponse = util.readHtml("classpath:templates/verify_success.html");
		mockMvc.perform(get("/verify")
				.param("code", verificationCode))
				.andExpect(status().isOk())
				.andExpect(content().string(expectedResponse));
		
		student = repo.findByEmail("mariorossi@gmail.com");
		assert(student.getVerificationCode() == null);
	}
	
	@Test
	public void register_1() throws Exception {

		// Send registration data
		RegistrationData data = new RegistrationData(
				"Mario","Rossi","mariorossi@gmail.com","Password","Password123@",
				Corso.Associate.toString());
		
		String expectedResponse = "Errore: I dati inseriti non sono corretti.";
		mockMvc.perform(post("/register")
					.flashAttr("registrationData", data))
					.andExpect(status().is(422))
					.andExpect(content().string(expectedResponse));
	}
	
	@Test
	public void register_2() throws Exception {
		
		// Inserisci studente con stessa email
		Student initStudent = new Student();
		initStudent.setEmail("mariorossi@gmail.com");
		repo.save(initStudent);
		
		// Send registration data
		RegistrationData data = new RegistrationData(
				"Mario","Rossi","mariorossi@gmail.com","Password123@","Password123@",
				Corso.Associate.toString());
		
		String expectedResponse = "Errore: Email già in uso.";
		mockMvc.perform(post("/register")
					.flashAttr("registrationData", data))
					.andExpect(status().is(422))
					.andExpect(content().string(expectedResponse));
	}
	
	@Test
	public void register_3() throws Exception {

		// Send registration data
		RegistrationData data = new RegistrationData(
				null,"Rossi","mariorossi@gmail.com","Password","Password123@",
				Corso.Associate.toString());
		
		String expectedResponse = "Errore: I dati inseriti non sono corretti.";
		mockMvc.perform(post("/register")
					.flashAttr("registrationData", data))
					.andExpect(status().is(422))
					.andExpect(content().string(expectedResponse));
	}
}

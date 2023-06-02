package test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import studentDB.StudentDbApplication;
import studentDB.entity.Student;
import studentDB.repository.StudentRepository;

@Import({TestConfig.class})
@SpringBootTest(classes = StudentDbApplication.class)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ForgotPasswordTests {
	
	@Autowired
	private StudentRepository repo;
	
	@Autowired
	private MockMvc mockMvc;
	
	@BeforeAll
	private void init() {	
		Student testStudent = new Student();
		testStudent.setEmail("mariorossi@gmail.com");
		testStudent.setPassword("{noop}Password123@");
		testStudent.setRole("USER");
		testStudent.setEnabled(true);
		testStudent.setVerificationCode(null);
		repo.save(testStudent);
	}
	
	@AfterAll
	private void tearDown() {
		Student student = repo.findByEmail("mariorossi@gmail.com");
		repo.delete(student);
	}
	
	@Test
	public void getForgotPasswordForm() throws Exception {
		
		String expectedResponse = util.readHtml("classpath:templates/forgot_password_form.html");
		mockMvc.perform(get("/forgot-password"))
				.andExpect(status().isOk())
				.andExpect(content().string(expectedResponse));
	}
	
	@Test
	public void forgotPassword_0() throws Exception {
		
		// Post a forgot-password
		String email = "mariorossi@gmail.com";
		String expectedResponse = "Email di reset password inviata";
		mockMvc.perform(post("/forgot-password")
				.param("email", email))
				.andExpect(status().isOk())
				.andExpect(content().string(expectedResponse));
		
		// Get reset-password?token
		String token = repo.findByEmail(email).getResetPasswordToken();
		mockMvc.perform(get("/reset-password")
				.param("token", token))
				.andExpect(status().isOk());
		
		// Post a reset-password
		String newPassword = "Password321@";
		expectedResponse = util.readHtml("classpath:templates/password_changed.html");
		mockMvc.perform(post("/reset-password")
				.param("password", newPassword)
				.param("confirmPassword", newPassword)
				.param("token", token))
				.andExpect(status().isOk())
				.andExpect(content().string(expectedResponse));
		
		Student student = repo.findByEmail(email);
		assert(student != null && student.isEnabled() && student.getPassword().equals(newPassword));
	}
	
	@Test
	public void forgotPassword_1() throws Exception {
		
		// Post a forgot-password
		String email = "mariorossi@gmail.com";
		String expectedResponse = "Email di reset password inviata";
		mockMvc.perform(post("/forgot-password")
				.param("email", email))
				.andExpect(status().isOk())
				.andExpect(content().string(expectedResponse));
		
		// Get reset-password?token
		String token = repo.findByEmail(email).getResetPasswordToken();
		mockMvc.perform(get("/reset-password")
				.param("token", token))
				.andExpect(status().isOk());
		
		// Post a reset-password
		Student student = repo.findByEmail(email);
		assert(student != null && student.isEnabled());
		String oldPassword = student.getPassword();
		
		String newPassword = "Password";
		expectedResponse = "Errore: Password non valida";
		mockMvc.perform(post("/reset-password")
				.param("password", newPassword)
				.param("confirmPassword", newPassword)
				.param("token", token))
				.andExpect(status().is(422))
				.andExpect(content().string(expectedResponse));
		
		 assert(student != null && student.isEnabled() && student.getPassword().equals(oldPassword));
	}
	
	@Test
	public void forgotPassword_2() throws Exception {
		
		// Post a forgot-password
		String email = "mario@gmail.com";
		String expectedResponse = "Errore: Impossibile trovare lo studente con email " + email;
		mockMvc.perform(post("/forgot-password")
				.param("email", email))
				.andExpect(status().is(404))
				.andExpect(content().string(expectedResponse));
	}
	
	@Test
	public void forgotPassword_3() throws Exception {
		
		// Crea studente non confermato
		String email = "mariorossi@gmail.com";
		Student student = repo.findByEmail(email);
		student.setEnabled(false);
		repo.save(student);
		
		// Post a forgot-password
		String expectedResponse = "Errore: Questa email non è stata ancora verificata";
		mockMvc.perform(post("/forgot-password")
				.param("email", email))
				.andExpect(status().is(422))
				.andExpect(content().string(expectedResponse));
		
		// Cleanup
		student.setEnabled(true);
		repo.save(student);
	}
	
	@Test
	public void forgotPassword_4() throws Exception {
		
		// Post a forgot-password
		String email = null;
		String expectedResponse = "Errore: Impossibile trovare lo studente con email " + email;
		mockMvc.perform(post("/forgot-password")
				.param("email", email))
				.andExpect(status().is(404))
				.andExpect(content().string(expectedResponse));
	}
}

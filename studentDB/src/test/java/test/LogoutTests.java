package test;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import studentDB.StudentDbApplication;
import studentDB.entity.Student;
import studentDB.repository.StudentRepository;

@Import({TestConfig.class})
@SpringBootTest(classes = StudentDbApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LogoutTests {
	
	private MockMvc mockMvc;
	
	@Autowired
	private StudentRepository repo;
	
	@Autowired
	private WebApplicationContext context;
	
	@BeforeAll
	private void init() {	
		mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
		
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
	void logout_0() throws Exception {

		// Effettua login
		String email = "mariorossi@gmail.com";
		String password = "Password123@";
		mockMvc.perform(formLogin("/login")
				.user(email)
				.password(password))
				.andExpect(status().is(302))
				.andExpect(redirectedUrl("/login-success"))
				.andExpect(authenticated().withUsername("mariorossi@gmail.com"));
		
		// Logout
		mockMvc.perform(logout("/logout"))
				.andExpect(status().is(302))
				.andExpect(redirectedUrl("/login?logout"))
				.andExpect(unauthenticated());
	}
	
	@Test
	void logout_1() throws Exception {
		
		// Logout
		mockMvc.perform(logout("/logout"))
				.andExpect(status().is(302))
				.andExpect(redirectedUrl("/login?logout"))
				.andExpect(unauthenticated());
	}

}

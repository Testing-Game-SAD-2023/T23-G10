package studentDB;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import myPackage.controller.StudentDbApplication;
import myPackage.entity.Student;
import myPackage.repository.StudentRepository;


@SpringBootTest(classes = StudentDbApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Import({TestConfig.class})
class LoginTests {
	
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
	public void getLoginForm() throws Exception {
		
		String expectedResponse = util.readHtml("classpath:templates/login.html");
		mockMvc.perform(get("/login"))
				.andExpect(status().isOk())
				.andExpect(content().string(expectedResponse));
	}
	
	@Test
	public void login_0() throws Exception {
		
		String email = "mariorossi@gmail.com";
		String password = "Password123@";
		mockMvc.perform(formLogin("/login")
				.user(email)
				.password(password))
				.andExpect(status().is(302))
				.andExpect(redirectedUrl("/login-success"))
				.andExpect(authenticated().withUsername("mariorossi@gmail.com"));
	}
	
	@Test
	public void login_1() throws Exception {
		
		String email = "mariorossi@gmail.com";
		String password = "Password";
		mockMvc.perform(formLogin("/login")
				.user(email)
				.password(password))
				.andExpect(status().is(302))
				.andExpect(redirectedUrl("/login?error"))
				.andExpect(unauthenticated());
	}
	
	@Test
	public void login_2() throws Exception {
		
		// Crea studente non confermato
		String email = "mariorossi@gmail.com";
		Student student = repo.findByEmail(email);
		student.setEnabled(false);
		repo.save(student);
		
		String password = "Password123@";
		mockMvc.perform(formLogin("/login")
				.user(email)
				.password(password))
				.andExpect(status().is(302))
				.andExpect(redirectedUrl("/login?error"))
				.andExpect(unauthenticated());
		
		// Cleanup
		student.setEnabled(true);
		repo.save(student);
	}
	
	@Test
	public void login_3() throws Exception {
		
		String email = null;
		String password = null;
		mockMvc.perform(formLogin("/login")
				.user(email)
				.password(password))
				.andExpect(status().is(302))
				.andExpect(redirectedUrl("/login?error"))
				.andExpect(unauthenticated());
	}
}

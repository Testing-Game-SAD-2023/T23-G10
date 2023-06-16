package studentDB.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Pattern;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.MailSendException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import studentDB.entity.Corso;
import studentDB.entity.RegistrationData;
import studentDB.entity.Student;
import studentDB.exception.StudentException;
import studentDB.service.MyUserDetails;
import studentDB.service.MyUserDetailsService;

@RestController
public class StudentController {
	
	@Autowired
	private MyUserDetailsService userDetailsService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Value("${t4.address}")
	private String T4Address;
	
	@Value("${t5.address}")
	private String T5Address;
	
	//Public//////////////////
	
	@GetMapping(value="/login", produces="text/html")
	public String getLoginForm(HttpServletRequest request) {
		
		String content = readHtml("templates/login.html");
		
		boolean logout = request.getParameterMap().containsKey("logout");
		if(logout) 
			content = content.replace("<!-- LOGOUT TEXT  -->", "Logout effettuato con successo <br/> <br/>");			
		
		boolean error = request.getParameterMap().containsKey("error");
		if(error)
			content = content.replace("<!-- ERROR TEXT  -->", "Email o password invalide. Controlla che il tuo account sia stato verificato. <br/> <br/>");
		return content;
	}
	
	@GetMapping("/login-success")
	public void loginSuccess(HttpServletResponse response) {
		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		String url = "/user/"+ userDetails.getId();
		
		try {
			response.sendRedirect(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@GetMapping("/user/{userId}")
	public String getUserAreaId(@PathVariable Long userId) {
		
		String content = readHtml("templates/user_area.html");
		content = content.replace("<<StudentId>>", userId.toString());
		content = content.replace("<<T4Address>>", T4Address);
		content = content.replace("<<T5Address>>", T5Address);
		return content;
		
	}
	
	@GetMapping("/authenticated-id")
	public String getAuthenticatedId(HttpSession session) {

	    if (session != null) {
	        SecurityContext securityContext = (SecurityContext) session.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
	        if (securityContext != null) {
	            Authentication authentication = securityContext.getAuthentication();
	            if (authentication != null && authentication.isAuthenticated()) {
	            	MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
	            	String userId = userDetails.getId().toString();
	                return userId;
	            }
	        }
	    }
	    return "-1";
	}

	
	@GetMapping(value="/register", produces="text/html")
	public String getRegisterForm() {
		return readHtml("templates/register.html");
	}
	
	@PostMapping(value="/register", produces="text/html")
	public String register(@ModelAttribute RegistrationData data,HttpServletRequest request) {	
		
		if(!checkRegistrationData(data)) 
			throw new StudentException("I dati inseriti non sono corretti.");
		
		Student newStudent = new Student(data.getNome(),data.getCognome(),
				Corso.valueOf(data.getCorso()),data.getEmail(),data.getPassword(),"USER");
		newStudent.setPassword(passwordEncoder.encode(newStudent.getPassword()));
		
		try {
			userDetailsService.createStudent(newStudent,request);
		} catch (MailSendException | UnsupportedEncodingException | MessagingException e) {
			e.printStackTrace();
			throw new StudentException("Impossibile inviare l'email di conferma. "
					+ "Controlla che l'indirizzo email inserito sia valido.");
		}
	
		return readHtml("templates/RegistrationSuccess.html");
	}
	
	@GetMapping(value="/verify", produces="text/html")
	public String verifyUser(@Param("code") String code) {
		
	    if(userDetailsService.verify(code)) 
	    	return readHtml("templates/verify_success.html");
	    else 
			return readHtml("templates/verify_fail.html");
	}
	
	//Admin///////////////////
	
	@GetMapping("/students")
	public List<Student> readAllStudents() {
		return userDetailsService.readAllStudents();
	}
	
	@GetMapping("/students/{id}")
	public Student readStudentById(@PathVariable Long id) {
		return userDetailsService.readStudentById(id);
	}

	@PutMapping("/students/{id}")
	public String updateStudent(@RequestBody Student newStudent, @PathVariable Long id) {
		newStudent.setPassword(passwordEncoder.encode(newStudent.getPassword()));
		return userDetailsService.updateStudent(newStudent, id);
	}

	@DeleteMapping("/students/{id}")
	public String deleteStudent(@PathVariable Long id) {
		return userDetailsService.deleteStudentById(id);
	}
	
	//Util////////////////////
	
	private String readHtml(String path) {
		
		String content = "";
		
		try {
			InputStream resource = new ClassPathResource(path).getInputStream();
			content = new String(resource.readAllBytes(), StandardCharsets.UTF_8);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		return content;
	}
	
	private boolean checkRegistrationData(RegistrationData data) {	
		
		//Controllo su nome e cognome
		String nome = data.getNome();
		String cognome = data.getCognome();
		if(nome == null || cognome == null) 
			return false;
		if(!nome.matches("[a-zA-Z]+") || !cognome.matches("[a-zA-Z]+"))
			return false;	
		
		//Controllo sul corso
		Corso[] corsoValues = Corso.values();
		String corso = data.getCorso();
		if(corso == null) return false;
		boolean corsoOk = false;
		for(int i=0;i<corsoValues.length;i++) {
			if(corso.equals(corsoValues[i].toString()))
				corsoOk = true;
		}
		if(!corsoOk) return false;
		
		//Controllo su email
	    try {
	        InternetAddress emailAddr = new InternetAddress(data.getEmail());
	        emailAddr.validate();
	    } catch (AddressException ex) {
	    	ex.printStackTrace();
	        return false;
	    }
		
	    //Controllo su password
	    String password = data.getPassword();
	    if(password == null || password.length()<8) return false;
	    
    	Pattern specialCharPatten = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
    	Pattern UpperCasePatten = Pattern.compile("[A-Z ]");
    	Pattern lowerCasePatten = Pattern.compile("[a-z ]");
    	Pattern digitCasePatten = Pattern.compile("[0-9 ]");

        if (!specialCharPatten.matcher(password).find() ||
        	!UpperCasePatten.matcher(password).find() ||
        	!lowerCasePatten.matcher(password).find() ||
       		!digitCasePatten.matcher(password).find()) 
        	return false;
	   
        if(!password.equals(data.getConfirmPassword()))
        	return false;
        
	    return true;
	}
}

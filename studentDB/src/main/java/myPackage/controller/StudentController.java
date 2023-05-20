package myPackage.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.util.List;
import java.util.regex.Pattern;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.mail.MailSendException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import myPackage.entity.Corso;
import myPackage.entity.Student;
import myPackage.entity.registrationData;
import myPackage.service.MyUserDetails;
import myPackage.service.MyUserDetailsService;
import myPackage.exception.StudentException;

@RestController
class StudentController {
	
	@Autowired
	MyUserDetailsService userDetailsService;
	
	@Autowired
	BCryptPasswordEncoder passwordEncoder;
	
	//Public//////////////////
	
	//Come prova
	@GetMapping("/user/{userId}")
	String readUserAreaId(@PathVariable Long userId) {
		return "Area dell'utente " + userId;
	}
	
	// Questo nell'ipotesi che dopo il login venga effettuato un accesso
	// all'url user/id dove l'id è appunto relativo all'utente che ha effettuato
	// l'autenticazione.
	@GetMapping("/login-success")
	void loginSuccess(HttpServletResponse response) {
		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String url = "/user/"+ userDetails.getId();
		
		try {
			response.sendRedirect(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@GetMapping(value="/register", produces="text/html")
	String getRegisterForm() {
		return readHtml("classpath:templates/register.html");
	}
	
	@GetMapping(value="/register-success", produces="text/html")
	String getRegistrationSuccess() {
		return readHtml("classpath:templates/RegistrationSuccess.html");
	}
	
	@PostMapping(value="/register", produces="text/html")
	String register(@ModelAttribute registrationData data,HttpServletRequest request) {	
		
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
	
		return readHtml("classpath:templates/RegistrationSuccess.html");
	}
	
	@GetMapping(value="/login", produces="text/html")
	String getLoginForm() {
		return readHtml("classpath:templates/login.html");
	}
	
	@GetMapping(value="/verify", produces="text/html")
	public String verifyUser(@Param("code") String code) {
		
	    if(userDetailsService.verify(code)) 
	    	return readHtml("classpath:templates/verify_success.html");
	    else 
			return readHtml("classpath:templates/verify_fail.html");
	}
	
	//Admin///////////////////
	
	@GetMapping("/students")
	List<Student> readAllStudents() {
		return userDetailsService.readAllStudents();
	}
	
	@GetMapping("/students/{id}")
	Student readStudentById(@PathVariable Long id) {
		return userDetailsService.readStudentById(id);
	}

	@PutMapping("/students/{id}")
	String updateStudent(@RequestBody Student newStudent, @PathVariable Long id) {
		newStudent.setPassword(passwordEncoder.encode(newStudent.getPassword()));
		return userDetailsService.updateStudent(newStudent, id);
	}

	@DeleteMapping("/students/{id}")
	String deleteStudent(@PathVariable Long id) {
		return userDetailsService.deleteStudentById(id);
	}
	
	//Util////////////////////
	
	private String readHtml(String path) {
		File file;
		String content = "";
		try {
			file = ResourceUtils.getFile(path);
			content = new String(Files.readAllBytes(file.toPath()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
        return content;
	}
	
	private boolean checkRegistrationData(registrationData data) {	
		
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
	   
        if(!password.equals(data.getConfirm_password()))
        	return false;
        
	    return true;
	}
}

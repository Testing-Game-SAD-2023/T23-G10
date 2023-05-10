package myPackage.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import myPackage.service.MyUserDetailsService;

@RestController
class StudentController {
	
	@Autowired
	MyUserDetailsService userDetailsService;
	
	@Autowired
	BCryptPasswordEncoder passwordEncoder;
	
	//Public//////////////////
	
	@GetMapping("/prova")
	String prova() {
		return "messaggio di prova";
	}
	
	@GetMapping(value="/register", produces="text/html")
	String getRegisterForm() {
		File file;
		String content = "";
		try {
			file = ResourceUtils.getFile("classpath:templates/register.html");
			content = new String(Files.readAllBytes(file.toPath()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
        return content;
	}
	
	@PostMapping("/register")
	String register(@ModelAttribute registrationData data) {
		Student newStudent = new Student(data.getNome(),data.getCognome(),
				Corso.valueOf(data.getCorso()),data.getEmail(),data.getPassword(),"USER");
		newStudent.setPassword(passwordEncoder.encode(newStudent.getPassword()));
		
		userDetailsService.createStudent(newStudent);
		
		File file;
		String content = "";
		try {
			file = ResourceUtils.getFile("classpath:templates/RegistrationSuccess.html");
			content = new String(Files.readAllBytes(file.toPath()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
        return content;
	}
	
	@GetMapping(value="/login", produces="text/html")
	String getLoginForm() {
		File file;
		String content = "";
		try {
			file = ResourceUtils.getFile("classpath:templates/login.html");
			content = new String(Files.readAllBytes(file.toPath()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
        return content;
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
}

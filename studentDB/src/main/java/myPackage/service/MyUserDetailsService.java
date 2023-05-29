package myPackage.service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import myPackage.entity.Student;
import myPackage.exception.StudentException;
import myPackage.exception.StudentNotFoundException;
import myPackage.repository.StudentRepository;
import net.bytebuddy.utility.RandomString;

@Service
public class MyUserDetailsService implements UserDetailsService {

	@Autowired
	private StudentRepository repo;
	
	@Autowired
	private MailService	mailService;
	 
	//Questo metodo è richiesto per l'autenticazione con Spring Security
	@Override
	public UserDetails loadUserByUsername(String userName) {
		
		//L'username nel nostro caso è l'email dello studente
		Student student = repo.findByEmail(userName);
		
		if(student == null) 
			throw new UsernameNotFoundException("Studente non trovato");
		
		return new MyUserDetails(student);
	}
	
	public String createStudent(Student student, HttpServletRequest request) throws UnsupportedEncodingException, MessagingException {
		
		if(repo.existsByEmail(student.getEmail())) 
			throw new StudentException("Email già in uso.");
		
		student.setCreationTimestamp(LocalDateTime.now());
		
	    String randomCode = RandomString.make(64);
	    student.setVerificationCode(randomCode);
	    student.setEnabled(false);
	       
	    mailService.sendVerificationEmail(student, mailService.getSiteURL(request));
	    
	    repo.save(student);
    	
		return "Email di conferma inviata";
	}
	
	public List<Student> readAllStudents() {
		return repo.findAll();
	}
	
	public Student readStudentById(Long id) {
		return repo.findById(id)
			.orElseThrow(() -> new StudentNotFoundException("Impossibile trovare lo studente con id "+id));
	}
	
	public String updateStudent(Student newStudent, Long id) {
		
		/*Controlla se esiste già uno studente (diverso da quello
		che stiamo modificando) che possiede la stessa email*/		
		Student other = repo.findByEmail(newStudent.getEmail());
		if(other != null && other.getId() != id) 
			throw new StudentException("Email già in uso");	
		
		newStudent.setId(id);
		repo.save(newStudent);
		
		if(repo.existsById(id))
			return "Studente aggiornato";
		else 
			return "Nuovo studente creato";	
	}
	
	public String deleteStudentById(Long id) {
		repo.deleteById(id);
		return "Studente cancellato";
	}
	
	public boolean verify(String verificationCode) {
	    Student user = repo.findByVerificationCode(verificationCode);
	     
	    if (user == null || user.isEnabled()) {
	        return false;
	    } else {
	        user.setVerificationCode(null);
	        user.setEnabled(true);
	        user.setCreationTimestamp(null);
	        repo.save(user);    
	        return true;
	    }	     
	}	
	
	public void updateResetPasswordToken(String token, String email) {
        Student student = repo.findByEmail(email);
        if (student == null) 
        	throw new StudentNotFoundException("Impossibile trovare lo studente con email " + email);
        else if (!student.isEnabled())
        	throw new StudentException("Questa email non è stata ancora verificata");
        else { 
        	student.setResetPasswordToken(token);
        	repo.save(student);
        }
    }
     
    public Student getByResetPasswordToken(String token) {
        return repo.findByResetPasswordToken(token);
    }
     
    public void updatePassword(Student student, String newPassword) {
        student.setResetPasswordToken(null);
        student.setPassword(newPassword);
        repo.save(student);
    }
}

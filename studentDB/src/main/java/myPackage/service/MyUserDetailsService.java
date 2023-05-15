	package myPackage.service;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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
	StudentRepository repo;
	
	@Autowired
	private JavaMailSender mailSender;
	 
	//Questo metodo è richiesto per l'autenticazione con Spring Security
	@Override
	public UserDetails loadUserByUsername(String userName) {
		
		//L'username nel nostro caso è l'email dello studente
		Optional<Student> student = repo.findByEmail(userName);
		
		student.orElseThrow(()-> new UsernameNotFoundException("Studente non trovato"));
		return student.map(MyUserDetails::new).get();
	}
	
	public String createStudent(Student student, HttpServletRequest request) throws UnsupportedEncodingException, MessagingException {
		
		if(repo.existsByEmail(student.getEmail())) 
			throw new StudentException("Email già in uso");
		
	    String randomCode = RandomString.make(64);
	    student.setVerificationCode(randomCode);
	    student.setEnabled(false);
	       
	    sendVerificationEmail(student, getSiteURL(request));
	    
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
		Optional<Student> other = repo.findByEmail(newStudent.getEmail());
		if(other.isPresent() && other.get().getId() != id) 
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
	        repo.save(user);    
	        return true;
	    }	     
	}
	
	private void sendVerificationEmail(Student student, String siteURL)
	        throws MessagingException, UnsupportedEncodingException {
	    String toAddress = student.getEmail();
	    String fromAddress = "progettosad@virgilio.it";
	    String senderName = "SAD Authentication Service";
	    String subject = "Please verify your registration";
	    String content = "Dear [[name]],<br>"
	            + "Please click the link below to verify your registration:<br>"
	            + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
	            + "Thank you,<br>";
	     
	    MimeMessage message = mailSender.createMimeMessage();
	    MimeMessageHelper helper = new MimeMessageHelper(message);
	     
	    helper.setFrom(fromAddress, senderName);
	    helper.setTo(toAddress);
	    helper.setSubject(subject);
	     
	    content = content.replace("[[name]]", student.getNome());
	    String verifyURL = siteURL + "/verify?code=" + student.getVerificationCode();
	     
	    content = content.replace("[[URL]]", verifyURL);
	     
	    helper.setText(content, true);
	     
	    mailSender.send(message);
	     
	}
	
	private String getSiteURL(HttpServletRequest request) {
		String siteURL = request.getRequestURL().toString();
		return siteURL.replace(request.getServletPath(), "");
	}	
}

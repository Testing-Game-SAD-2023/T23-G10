package studentDB.service;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import studentDB.entity.Student;

public interface MyUserDetailsService extends UserDetailsService {

	//Questo metodo è richiesto per l'autenticazione con Spring Security
	UserDetails loadUserByUsername(String userName);

	String createStudent(Student student, HttpServletRequest request)
			throws UnsupportedEncodingException, MessagingException;

	List<Student> readAllStudents();

	Student readStudentById(Long id);

	String updateStudent(Student newStudent, Long id);

	String deleteStudentById(Long id);

	boolean verify(String verificationCode);

	void updateResetPasswordToken(String token, String email, HttpServletRequest request);

	Student getByResetPasswordToken(String token);

	void updatePassword(Student student, String newPassword);

}
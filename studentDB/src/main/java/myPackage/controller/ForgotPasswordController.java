package myPackage.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.MailSendException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import myPackage.entity.Student;
import myPackage.exception.StudentException;
import myPackage.service.MailService;
import myPackage.service.MyUserDetailsService;
import net.bytebuddy.utility.RandomString;

@RestController
public class ForgotPasswordController {

	@Autowired
	MyUserDetailsService userDetailsService;
	
	@Autowired
	BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
    MailService mailService;
	
	@GetMapping(value="/forgot_password", produces="text/html")
    public String getForgotPasswordForm() {
		return readHtml("classpath:templates/forgot_password_form.html");
    }
 
    @PostMapping("/forgot_password")
    public String processForgotPassword(HttpServletRequest request) {
        
    	String email = request.getParameter("email");
    	String token = RandomString.make(64);
        userDetailsService.updateResetPasswordToken(token, email);
        
        String resetPasswordLink = mailService.getSiteURL(request) + "/reset_password?token=" + token;
        try {
			mailService.sendResetEmail(email, resetPasswordLink);
		} catch (MailSendException | UnsupportedEncodingException | MessagingException e) {
			e.printStackTrace();
			throw new StudentException("Impossibile inviare l'email di reset password. "
					+ "Controlla che l'indirizzo email inserito sia valido.");
		}         
             
        return "Email di reset password inviata";
    }
     
    @GetMapping(value="/reset_password", produces="text/html")
    public String getResetPasswordForm(@Param("token") String token) {
    	Student student = userDetailsService.getByResetPasswordToken(token);
         
        if (student == null) 
            throw new StudentException("Invalid token");
        
        String page = readHtml("classpath:templates/reset_password_form.html");
    	page.replace("[[token]]", token);
        return page;
    }
     
    @PostMapping("/reset_password")
    public String processResetPassword(HttpServletRequest request) {
    	String token = request.getParameter("token");
        String password = request.getParameter("password");
         
        Student student = userDetailsService.getByResetPasswordToken(token);
         
        if (student == null)         
        	throw new StudentException("Invalid token");
        
        userDetailsService.updatePassword(student, passwordEncoder.encode(password));
             
        return "Password cambiata con successo";
    }
	
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
}

package studentDB.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import net.bytebuddy.utility.RandomString;
import studentDB.entity.Student;
import studentDB.exception.StudentException;
import studentDB.service.MyUserDetailsService;

@RestController
public class ForgotPasswordController {

	@Autowired
	private MyUserDetailsService userDetailsService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@GetMapping(value="/forgot-password", produces="text/html")
    public String getForgotPasswordForm() {
		return readHtml("classpath:templates/forgot_password_form.html");
    }
 
    @PostMapping("/forgot-password")
    public String processForgotPassword(HttpServletRequest request) {
        
    	String email = request.getParameter("email");
    	String token = RandomString.make(64);
        userDetailsService.updateResetPasswordToken(token, email, request);        
             
        return "Email di reset password inviata";
    }
     
    @GetMapping(value="/reset-password", produces="text/html")
    public String getResetPasswordForm(@Param("token") String token) {
    	
    	if (token == null) 
            return readHtml("classpath:templates/invalid_token.html");
    	
    	Student student = userDetailsService.getByResetPasswordToken(token);
         
        if (student == null) 
            return readHtml("classpath:templates/invalid_token.html");
        
        String page = readHtml("classpath:templates/reset_password_form.html");
    	return page.replace("[[token]]", token);
    }
     
    @PostMapping(value="/reset-password", produces="text/html")
    public String processResetPassword(HttpServletRequest request) {
    	String token = request.getParameter("token");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        
        if(!checkPassword(password))
        	throw new StudentException("Password non valida");
        if(!password.equals(confirmPassword))
        	throw new StudentException("Le password non coincidono");
        
        Student student = userDetailsService.getByResetPasswordToken(token);
        if (student == null)         
        	return readHtml("classpath:templates/invalid_token.html");
        
        userDetailsService.updatePassword(student, passwordEncoder.encode(password));
             
        return readHtml("classpath:templates/password_changed.html");
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
	
	private boolean checkPassword(String password) {
		
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
        
	    return true;
	}
}

package studentDB.service;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import studentDB.entity.Student;

@Service
public class MailServiceImpl implements MailService {
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Value("${spring.mail.username}")
	private String mailFrom;
	
	@Override
	public void sendVerificationEmail(Student student, String siteURL)
	        throws MessagingException, UnsupportedEncodingException {
	    String toAddress = student.getEmail();
	    String subject = "Verifica il tuo account";
	    String content = "[[name]],<br>"
	            + "clicca il link qui sotto per confermare la registrazione:<br>"
	            + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFICA</a></h3>"
	            + "Grazie.<br>";
	     
	    MimeMessage message = mailSender.createMimeMessage();
	    MimeMessageHelper helper = new MimeMessageHelper(message);
	     
	    helper.setFrom(mailFrom, "Testing-Game-SAD-2023");
	    helper.setTo(toAddress);
	    helper.setSubject(subject);
	     
	    content = content.replace("[[name]]", student.getNome());
	    String verifyURL = siteURL + "/verify?code=" + student.getVerificationCode();
	     
	    content = content.replace("[[URL]]", verifyURL);
	     
	    helper.setText(content, true);
	     
	    mailSender.send(message);
	     
	}
	
	@Override
	public void sendResetEmail(String recipientEmail, String link)
	        throws MessagingException, UnsupportedEncodingException {
	    MimeMessage message = mailSender.createMimeMessage();              
	    MimeMessageHelper helper = new MimeMessageHelper(message);
	     
	    helper.setFrom(mailFrom, "Testing-Game-SAD-2023");
	    helper.setTo(recipientEmail);
	     
	    String subject = "Reset password";
	     
	    String content = "<p>Ciao,</p>"
	            + "<p>E' stata fatta una richiesta per cambiare la password.</p>"
	            + "<p>Clicca il link qui sotto per procedere:</p>"
	            + "<p><a href=\"" + link + "\">Cambia password</a></p>"
	            + "<br>"
	            + "<p>Ignora questa email se ricordi la tua password, "
	            + "o se non sei stato tu a fare la richiesta.</p>";
	     
	    helper.setSubject(subject);
	     
	    helper.setText(content, true);
	     
	    mailSender.send(message);
	}
	
	@Override
	public String getSiteURL(HttpServletRequest request) {
		String siteURL = request.getRequestURL().toString();
		return siteURL.replace(request.getServletPath(), "");
	}	
	
}

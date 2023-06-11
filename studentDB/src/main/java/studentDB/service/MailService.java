package studentDB.service;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import studentDB.entity.Student;

public interface MailService {

	void sendVerificationEmail(Student student, String siteURL) throws MessagingException, UnsupportedEncodingException;

	void sendResetEmail(String recipientEmail, String link) throws MessagingException, UnsupportedEncodingException;

	String getSiteURL(HttpServletRequest request);

}
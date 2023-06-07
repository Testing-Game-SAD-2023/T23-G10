package test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ResourceUtils;

import studentDB.entity.RegistrationData;
import studentDB.entity.Student;

public class util {
	
	public static String readHtml(String path) {
		
		String content = "";
		
		try {
			InputStream resource = new ClassPathResource(path).getInputStream();
			content = new String(resource.readAllBytes(), StandardCharsets.UTF_8);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		return content;
	}
	
	public static boolean checkEquals(Student student, RegistrationData data) {
		return
				student.getNome().equals(data.getNome()) &&
				student.getCognome().equals(data.getCognome()) &&
				student.getEmail().equals(data.getEmail()) &&
				student.getPassword().equals(data.getPassword()) &&
				student.getCorso().toString().equals(data.getCorso());
	}
}

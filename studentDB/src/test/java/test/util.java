package test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.springframework.util.ResourceUtils;

import studentDB.entity.RegistrationData;
import studentDB.entity.Student;

public class util {
	
	public static String readHtml(String path) {
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
	
	public static boolean checkEquals(Student student, RegistrationData data) {
		return
				student.getNome().equals(data.getNome()) &&
				student.getCognome().equals(data.getCognome()) &&
				student.getEmail().equals(data.getEmail()) &&
				student.getPassword().equals(data.getPassword()) &&
				student.getCorso().toString().equals(data.getCorso());
	}
}

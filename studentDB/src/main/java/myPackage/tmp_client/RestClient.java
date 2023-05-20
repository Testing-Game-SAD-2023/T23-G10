package myPackage.tmp_client;

import org.springframework.web.client.RestTemplate;

import myPackage.entity.*;

import java.util.Arrays;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;

public class RestClient {
	private static final String BASE = "http://localhost:8080";
	private static final String READ_ALL_STUDENTS = BASE + "/students";
	private static final String READ_STUDENT_BY_ID = BASE + "/students/{id}";
	private static final String UPDATE_STUDENT = BASE + "/students/{id}";
	private static final String CREATE_STUDENT = BASE + "/l";
	private static final String DELETE_STUDENT_BY_ID = BASE + "/students/{id}";
	static RestTemplate client = new RestTemplate();

	public static void main(String[] args) {
		Student studente = new Student("Paolo", "Rossi", Corso.Bachelor, "mariorossi@gmail.com", "lamiapass", "USER");
		// createStudent(studente);
		updateStudent("3", studente);
		// System.out.println(readStudentById("1").getNome());
		// deleteStudentById("2");
		// login();
		// System.out.println(updateStudent("2",studente));
		// System.out.println(readStudentById("1"));

		// System.out.println(readAllStudents());
		// Gestire la situazione in cui si prova a registrare
		// un utente già presente.
	}

	private static Student readStudentById(String id) {

		return client.getForObject(READ_STUDENT_BY_ID, Student.class, id);

	}

	private static String readAllStudents() {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setBasicAuth("username", "password");
		HttpEntity<String> entity = new HttpEntity<>("", headers);

		ResponseEntity<String> response = client.exchange("http://localhost:8080/register", HttpMethod.GET, entity,
				String.class);
		return response.getBody();
	}

	private static void login() {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setBasicAuth("admin", "pass");
		HttpEntity<String> entity = new HttpEntity<>("", headers);

		ResponseEntity<String> response = client.exchange("http://localhost:8080/login", HttpMethod.POST, entity,
				String.class);

	}

	private static void updateStudent(String id, Student student) {

		client.put(UPDATE_STUDENT, student, id);

	}

	private static void deleteStudentById(String id) {

		client.delete(DELETE_STUDENT_BY_ID, id);

	}
}

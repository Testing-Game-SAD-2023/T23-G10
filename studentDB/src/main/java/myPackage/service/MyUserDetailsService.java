	package myPackage.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import myPackage.entity.Student;
import myPackage.exception.StudentException;
import myPackage.exception.StudentNotFoundException;
import myPackage.repository.StudentRepository;

@Service
public class MyUserDetailsService implements UserDetailsService {

	@Autowired
	StudentRepository repo;
	
	//Questo metodo è richiesto per l'autenticazione con Spring Security
	@Override
	public UserDetails loadUserByUsername(String userName) {
		
		//L'username nel nostro caso è l'email dello studente
		Optional<Student> student = repo.findByEmail(userName);
		
		student.orElseThrow(()-> new UsernameNotFoundException("Studente non trovato"));
		return student.map(MyUserDetails::new).get();
	}
	
	public String createStudent(Student student) {
		
		if(repo.existsByEmail(student.getEmail())) 
			throw new StudentException("Email già in uso");
		
		repo.save(student);
		return "Studente registrato";
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
}

package studentDB;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import entity.Student;

@RestController
class StudentController {

	private final StudentRepository repository;
	
	StudentController(StudentRepository repository) {
		this.repository = repository;
	}

	@GetMapping("/students")
	List<Student> all() {
		return repository.findAll();
	}

	@PostMapping("/students")
	Student newStudent(@RequestBody Student newStudent) {
		return repository.save(newStudent);
	}

	
	//Single item
	
	@GetMapping("/students/{id}")
	Student one(@PathVariable Long id) {
		
		return repository.findById(id)
			.orElseThrow(() -> new StudentNotFoundException(id));
	}

	@PutMapping("/students/{id}")
	Student replaceStudent(@RequestBody Student newStudent, @PathVariable Long id) {
		
		return repository.findById(id)
			.map(student -> {
				student.setNome(newStudent.getNome());
				student.setCognome(newStudent.getCognome());
				student.setEmail(newStudent.getEmail());
				student.setPassword(newStudent.getPassword());
				student.setCorso(newStudent.getCorso());
				return repository.save(student);
			})
			.orElseGet(() -> {
				newStudent.setId(id);
				return repository.save(newStudent);
			});
	}

	@DeleteMapping("/students/{id}")
	void deleteStudent(@PathVariable Long id) {
		
		repository.deleteById(id);
	}
}

package myPackage.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import myPackage.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
	Optional<Student> findByEmail(String email);
	boolean existsByEmail(String email);
}

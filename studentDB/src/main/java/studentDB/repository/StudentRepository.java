package studentDB.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import studentDB.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
	Student findByEmail(String email);
	boolean existsByEmail(String email);
	Student findByVerificationCode(String code);
	Student findByResetPasswordToken(String token);
}


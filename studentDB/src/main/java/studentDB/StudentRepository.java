package studentDB;

import org.springframework.data.jpa.repository.JpaRepository;

import entity.Student;

interface StudentRepository extends JpaRepository<Student, Long> {

}

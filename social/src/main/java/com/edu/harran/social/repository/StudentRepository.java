package com.edu.harran.social.repository;

import com.edu.harran.social.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student,Long> {
    List<Student> findByIsDeletedFalse();
    Student findByStudentId(String studentId);
}

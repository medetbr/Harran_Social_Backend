package com.edu.harran.social.repository;

import com.edu.harran.social.entity.Department;
import com.edu.harran.social.entity.Program;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<Department,Long> {
    Department findByName(String name);

    Department findByDepartmentId(String departmentId);

    List<Department> findByIsDeletedFalse();
}

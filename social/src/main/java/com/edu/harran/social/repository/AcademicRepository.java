package com.edu.harran.social.repository;

import com.edu.harran.social.entity.Academic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AcademicRepository extends JpaRepository<Academic,Long> {
    Academic findByName(String name);

    Academic findByAcademicId(String academicId);
}

package com.edu.harran.social.repository;

import com.edu.harran.social.entity.Program;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProgramRepository extends JpaRepository<Program,Long> {
    Program findByName(String name);
    Program findByProgramId(String programId);
}

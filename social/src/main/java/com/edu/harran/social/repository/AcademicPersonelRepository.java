package com.edu.harran.social.repository;

import com.edu.harran.social.entity.AcademicPersonel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AcademicPersonelRepository extends JpaRepository<AcademicPersonel,Long> {
    List<AcademicPersonel> findByIsDeletedFalse();

    AcademicPersonel findByAcademicPersonelId(String academicPersonelId);

    @Query("select a from AcademicPersonel a where a.department.departmentId=:id")
    List<AcademicPersonel> findByDepartmentId(@Param("id") String departmentId);

    @Query("select a from AcademicPersonel a where a.name LIKE %:query%")
    List<AcademicPersonel> findByQueryOfName(@Param("query")String query, Pageable page);
}

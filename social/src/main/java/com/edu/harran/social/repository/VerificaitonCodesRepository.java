package com.edu.harran.social.repository;

import com.edu.harran.social.entity.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificaitonCodesRepository extends JpaRepository< VerificationCode,Long> {
    @Query("SELECT vc FROM VerificationCode vc WHERE vc.email = :email ORDER BY vc.createdAt DESC LIMIT 1")
    VerificationCode findByCode(@Param("email") String email);
}

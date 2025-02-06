package com.edu.harran.social.repository;

import com.edu.harran.social.entity.UserApprovalPending;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserApprovalPendingRepository extends JpaRepository<UserApprovalPending,Long> {
    UserApprovalPending findByEmail(String email);

    @Query("SELECT u from UserApprovalPending u where u.user_approval_pending_id=:id")
    UserApprovalPending findByUserApprovalPendingId(@Param("id") String userApprovalPendingId);
}

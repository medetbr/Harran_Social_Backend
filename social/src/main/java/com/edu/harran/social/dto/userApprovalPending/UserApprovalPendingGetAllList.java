package com.edu.harran.social.dto.userApprovalPending;

import com.edu.harran.social.entity.UserApprovalPendingStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserApprovalPendingGetAllList {
    private String user_approval_pending_id;
    private String email;
    private UserApprovalPendingStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

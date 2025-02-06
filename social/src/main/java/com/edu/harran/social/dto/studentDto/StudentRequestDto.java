package com.edu.harran.social.dto.studentDto;

import lombok.Data;

@Data
public class StudentRequestDto {
    private String userApprovalPendingId;
    private String name;
    private String surname;
    private String email;
    private String departmentId;
}

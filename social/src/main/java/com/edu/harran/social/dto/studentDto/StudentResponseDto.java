package com.edu.harran.social.dto.studentDto;

import com.edu.harran.social.dto.DepartmentDto.DepartmentResponseDto;
import com.edu.harran.social.dto.userDto.UserResponseDto;
import lombok.Data;

@Data
public class StudentResponseDto {
    private String studentId;
    private DepartmentResponseDto department;
    private UserResponseDto user;
    private Boolean isDeleted;
}

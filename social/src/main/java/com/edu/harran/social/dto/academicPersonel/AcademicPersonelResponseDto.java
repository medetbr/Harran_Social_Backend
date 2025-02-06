package com.edu.harran.social.dto.academicPersonel;

import com.edu.harran.social.dto.DepartmentDto.DepartmentResponseDto;
import lombok.Data;

@Data
public class AcademicPersonelResponseDto {
    private String name;
    private String e_mail;
    private String academicPersonelId;
    private DepartmentResponseDto department;
    private Boolean isDeleted;
}

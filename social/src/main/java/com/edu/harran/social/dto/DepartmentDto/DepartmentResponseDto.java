package com.edu.harran.social.dto.DepartmentDto;

import com.edu.harran.social.dto.programDto.ProgramResponseDto;
import lombok.Data;

@Data
public class DepartmentResponseDto {
    private String name;
    private String e_mail;
    private String departmentId;
    private ProgramResponseDto program;
    private Boolean isDeleted;
}

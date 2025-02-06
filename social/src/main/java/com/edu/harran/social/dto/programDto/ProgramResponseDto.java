package com.edu.harran.social.dto.programDto;

import com.edu.harran.social.dto.academicDto.AcademicResponseDto;
import lombok.Data;

@Data
public class ProgramResponseDto {
    private String name;
    private String e_mail;
    private String programId;
    private AcademicResponseDto academic;
    private Boolean isDeleted;
}

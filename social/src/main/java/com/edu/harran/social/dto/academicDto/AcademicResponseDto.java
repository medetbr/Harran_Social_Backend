package com.edu.harran.social.dto.academicDto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AcademicResponseDto {
    private String name;
    private String academicId;
    private Boolean isDeleted;
}

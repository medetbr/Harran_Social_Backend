package com.edu.harran.social.dto.messageDto;

import lombok.Data;

@Data
public class DocumentResponseDto {
    private String fileName;
    private Long fileSize;
    private String originalName;
    private String fileUrl;
    private String page;
}

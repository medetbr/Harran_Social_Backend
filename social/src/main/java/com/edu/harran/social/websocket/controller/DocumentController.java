package com.edu.harran.social.websocket.controller;

import com.edu.harran.social.websocket.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api/documents/")
@RequiredArgsConstructor
@CrossOrigin(origins = "*",allowedHeaders = "*")
public class DocumentController {
    private final DocumentService documentService;

    @GetMapping("/download/{chatId}/{filename}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename,@PathVariable String chatId) {
        return documentService.documentDownload(filename,chatId);
    }

}

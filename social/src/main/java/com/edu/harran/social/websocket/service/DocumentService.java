package com.edu.harran.social.websocket.service;

import com.edu.harran.social.service.FileService;
import com.edu.harran.social.websocket.entity.Document;
import com.edu.harran.social.websocket.entity.Message;
import com.edu.harran.social.websocket.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;



@Service
@RequiredArgsConstructor
public class DocumentService {
    private final FileService fileService;
    private final DocumentRepository documentRepository;

    public void createDocument(Message createdMessage, MultipartFile document,String page) {
        try {
            String documentFileName = fileService.uploadDocumentForChat(createdMessage.getChat().getChatId(), document);
            Document doc = new Document();
            doc.setMessage(createdMessage);
            doc.setFileName(documentFileName);
            doc.setOriginalName(document.getOriginalFilename());
            doc.setFileSize(document.getSize());
            doc.setPage(page);
            documentRepository.save(doc);
        } catch (Exception e) {
            throw new RuntimeException("Error creating document", e);
        }
    }

    public Document getDocumentByMessage(Message msg) {
        return documentRepository.findByMessage(msg);
    }

//    @GetMapping("/preview")
//    public ResponseEntity<Resource> getDocumentPreview(@RequestParam String fileName) {
//        try {
//            byte[] previewImage = documentService.getDocumentPreview(fileName);
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.IMAGE_PNG);
//            return new ResponseEntity<>(new ByteArrayResource(previewImage), headers, HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }

    public ResponseEntity<Resource> documentDownload(String fileName, String chatId) {
        Document document = documentRepository.findByFileName(fileName);
        return fileService.documentDownload(fileName, chatId, document.getOriginalName());
    }
}

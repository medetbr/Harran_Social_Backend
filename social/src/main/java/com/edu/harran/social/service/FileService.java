package com.edu.harran.social.service;

import com.edu.harran.social.websocket.entity.Chat;
import com.edu.harran.social.websocket.entity.Document;
import jakarta.mail.internet.MimeUtility;
import jakarta.transaction.Transactional;
import org.apache.tomcat.util.file.ConfigurationSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class FileService {
    private final String BASE_DIRECTORY_PATH = "C:\\Users\\Medet\\Desktop\\Java\\social\\social\\src\\main\\java\\com\\edu\\harran\\social\\files";

    public void createFileForChat(Chat chat) throws IOException {
        String targetFolderPath = BASE_DIRECTORY_PATH+ "\\chats\\"  + chat.getChatId();
        File chatDirectory = new File(targetFolderPath);

        if (!chatDirectory.exists()) {
            chatDirectory.mkdir();
        }
    }
    public String uploadImageForChatProfile(Chat chat, MultipartFile image) throws IOException{
        String targetFolderPath = BASE_DIRECTORY_PATH+ "\\chats\\"  + chat.getChatId() + "\\profile-img\\";
        File imageDirectory = new File(targetFolderPath);

        if (!imageDirectory.exists()) {
            imageDirectory.mkdir();
        }

        String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSS"));
        String originalFilename = image.getOriginalFilename();
        String extension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf('.')) : "";

        File serverFile = new File(targetFolderPath, timeStamp + extension);

        image.transferTo(serverFile);
        return timeStamp+extension;
    }

    public ResponseEntity<?> getChatProfileImg(String chatId, String fileName) {
        Path filePath = Paths.get(BASE_DIRECTORY_PATH + "\\chats\\" + chatId + "\\profile-img\\" + fileName);
        if (Files.exists(filePath)) {
            try {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG).
                        contentType(MediaType.IMAGE_PNG)
                        .body(Files.readAllBytes(filePath));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public String uploadDocumentForChat(String chatId, MultipartFile document) throws IOException{

        String[] parts = UUID.randomUUID().toString().split("-");

        String targetFolderPath = BASE_DIRECTORY_PATH+ "\\chats\\"  + chatId + "\\document\\";
        File documentDirectory = new File(targetFolderPath);

        if (!documentDirectory.exists()) {
            documentDirectory.mkdir();
        }

        String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSS"));
        String originalFilename = document.getOriginalFilename();
        String extension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf('.')) : "";

        String fileName = timeStamp+"-"+parts[parts.length-1] + extension;
        File serverFile = new File(targetFolderPath, fileName);

        document.transferTo(serverFile);
         return fileName;
    }

    public ResponseEntity<Resource> documentDownload(String fileName,String chatId,String originalName) {
        try {
            // Dosya yolunu oluştur
            Path filePath = Paths.get(BASE_DIRECTORY_PATH + "\\chats\\" + chatId + "\\document\\" + fileName);
            Resource resource = new UrlResource(filePath.toUri());

            // Dosyanın mevcut olup olmadığını kontrol et
            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }

            // Dosya indirimi için uygun başlıkları ayarla
            String encodedFilename = MimeUtility.encodeText(originalName, "UTF-8", "B");
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFilename  + "\"")
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }
}

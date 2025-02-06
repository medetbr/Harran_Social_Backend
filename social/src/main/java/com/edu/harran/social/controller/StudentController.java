package com.edu.harran.social.controller;

import com.edu.harran.social.dto.studentDto.StudentCreateRequestDto;
import com.edu.harran.social.dto.studentDto.StudentRequestDto;
import com.edu.harran.social.dto.studentDto.StudentResponseDto;
import com.edu.harran.social.entity.UserApprovalPending;
import com.edu.harran.social.entity.UserApprovalPendingStatus;
import com.edu.harran.social.entity.VerificationCode;
import com.edu.harran.social.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@RestController
@RequestMapping("/v1/api/")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class StudentController {

    private final StudentService studentService;
    private final JavaMailSender mailSender;
    private final VerificationCodesService verificationCodesService;
    private final UserService userService;
    private final UserApprovalPendingService userApprovalPendingService;


    @PostMapping("students/create")
    public void create(
            @RequestParam("studentRequestJson") String studentRequestDtoJson,
            @RequestParam("file") MultipartFile file
    ){
        if (!file.isEmpty()) {
            try {
                int fileExtensionIndex = file.getOriginalFilename().lastIndexOf(".");
                String fileExtension = file.getOriginalFilename().substring(fileExtensionIndex );
                byte[] bytes = file.getBytes();
                ObjectMapper objectMapper = new ObjectMapper();
                StudentCreateRequestDto studentRequestDto;
                studentRequestDto = objectMapper.readValue(studentRequestDtoJson, StudentCreateRequestDto.class);
                String fileId = UUID.randomUUID().toString();
                userApprovalPendingService.create(studentRequestDto,fileId);
                Path path = Paths.get("C:\\Users\\Medet\\Desktop\\Java\\social\\social\\src\\main\\java\\com\\edu\\harran\\social\\uploads" + File.separator + fileId + fileExtension );
                Files.write(path, bytes);
            } catch (Exception e) {
                System.out.println("Hata: " + e.getMessage());
            }

        }
    }
     public void sendEmail(String to, String subject, String text) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text,true);

        mailSender.send(message);
     }
    @PostMapping("/students/create/verification-code/{code}")
    public ResponseEntity<String> verificationCodeCheck(@PathVariable String code,@RequestBody StudentCreateRequestDto studentRequestDto) {
        try{
            VerificationCode verificationCode = verificationCodesService.checkCode(studentRequestDto.getStudentMail());
            if(verificationCode!=null && code.equals(String.valueOf(verificationCode.getCode()))){
                return ResponseEntity.status(HttpStatus.OK).body("Doğrulama başarılı. Kaydınız, onay için en kısa sürede incelenecektir.");
            }else throw new Exception("Doğrulama kodu geçersiz");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @PostMapping("/students/create/send-verification-code/{email}")
    public ResponseEntity<String> sendToEmailVerificationCode(@PathVariable String email) {
        try {
            UserApprovalPending userApprovalPending = userApprovalPendingService.getByOneEmail(email);
            if(userService.findUserByEmail(email)!=null){
                throw new Exception("Bu mail adında bir kullanıcı zaten mevcut");
            }else if(userApprovalPending!=null && userApprovalPending.getStatus() != UserApprovalPendingStatus.REJECTED){
                throw new Exception("Bu mail adında onay bekleyen bir kayıt zaten var");
            }
            Random random = new Random();
            int vCode = random.nextInt(900000) + 100000;
            verificationCodesService.create(vCode,email);
            String emailBody = "<h2 style=\"text-align: center;\">Harran Social Uygulaması Doğrulama Kodu</h2>"
                    + "<p style=\"text-align: center;\">Doğrulama kodunuz:</p>"
                    + "<h1 style=\"text-align: center; font-weight: bold;\">" + vCode + "</h1>";
            sendEmail(email.trim(),"Doğrulama Kodu Harran Social",emailBody);
            return ResponseEntity.status(HttpStatus.OK).body("Mail adresinize 6 haneli doğrulama kodu gönderilmiştir.");
        } catch ( Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @Transactional
    @PostMapping("/admin/students/created-student")
    public void created(
            @RequestBody StudentRequestDto studentRequestDto,
            @RequestHeader("Authorization") String token
    ){
        studentService.create(studentRequestDto,token);
    }

    @GetMapping("admin/students/all")
    public List<StudentResponseDto> getAll() {
        return studentService.findAll();
    }

    @GetMapping("students")
    public StudentResponseDto getStudent(@RequestHeader("Authorization") String token) {
        return studentService.findOneStudent(token);
    }

    @DeleteMapping("admin/students/delete/{studentId}")
    public void delete(@PathVariable String studentId) {
        studentService.delete(studentId);
    }

    @PatchMapping("admin/students/update/{studentId}")
    public void update(@PathVariable String studentId, @RequestBody StudentRequestDto studentRequestDto) {
        studentService.update(studentId, studentRequestDto);
    }
}

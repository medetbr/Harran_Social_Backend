package com.edu.harran.social.service;

import com.edu.harran.social.dto.studentDto.StudentCreateRequestDto;
import com.edu.harran.social.entity.UserApprovalPending;
import com.edu.harran.social.entity.UserApprovalPendingStatus;
import com.edu.harran.social.repository.UserApprovalPendingRepository;
import com.edu.harran.social.dto.userApprovalPending.UserApprovalPendingGetAllList;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserApprovalPendingService {
    private final UserApprovalPendingRepository userApprovalPendingRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public void create(StudentCreateRequestDto studentRequestDto, String id) {
        UserApprovalPending userApprovalPending =
                UserApprovalPending.
                        builder().
                        email(studentRequestDto.getStudentMail()).
                        user_approval_pending_id(id).
                        status(UserApprovalPendingStatus.PENDING).
                        password(passwordEncoder.encode(studentRequestDto.getPassword())).
                        build();
        userApprovalPendingRepository.save(userApprovalPending);
    }

    public UserApprovalPending getByOneEmail(String email) {
        return userApprovalPendingRepository.findByEmail(email);
    }

    public List<UserApprovalPendingGetAllList> findAllList() {
        List<UserApprovalPending> allList = userApprovalPendingRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        List<UserApprovalPendingGetAllList> allListDto = new ArrayList<>();

        for(UserApprovalPending item : allList) allListDto.add(modelMapper.map(item,UserApprovalPendingGetAllList.class));
        return allListDto;
    }

    public ByteArrayResource findDocumentByUserApprovalPendingId(String id) {
        try{
            Path filePath = Paths.get("C:\\Users\\Medet\\Desktop\\Java\\social\\social\\src\\main\\java\\com\\edu\\harran\\social\\uploads" + File.separator +id +".pdf" );
            byte[] fileBytes = Files.readAllBytes(filePath);

            // Byte dizisini kaynak olarak oluştur
            ByteArrayResource resource = new ByteArrayResource(fileBytes);
            return resource;
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    public void rejectByUserApprovalPendingId(String id){
        try {
            UserApprovalPending userApprovalPending = userApprovalPendingRepository.findByUserApprovalPendingId(id);
            if(userApprovalPending==null) throw new Exception("Geçersiz id");
            if(userApprovalPending.getStatus()!=UserApprovalPendingStatus.REJECTED){
                userApprovalPending.setStatus(UserApprovalPendingStatus.REJECTED);
                this.save(userApprovalPending);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    public UserApprovalPending findByUserApprovalPendingId(String serApprovalPendingIdu) {
        return userApprovalPendingRepository.findByUserApprovalPendingId(serApprovalPendingIdu);
    }
    public void save(UserApprovalPending userApprovalPending){
        userApprovalPendingRepository.save(userApprovalPending);
    }
}

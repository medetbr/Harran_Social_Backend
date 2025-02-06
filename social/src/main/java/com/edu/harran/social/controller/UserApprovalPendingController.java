package com.edu.harran.social.controller;

import com.edu.harran.social.dto.userApprovalPending.UserApprovalPendingGetAllList;
import com.edu.harran.social.service.UserApprovalPendingService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/student-approval-pending/")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserApprovalPendingController {
    private final UserApprovalPendingService userApprovalPendingService;

    @GetMapping("/admin/all-list")
    public List<UserApprovalPendingGetAllList> getAllList(){
    return userApprovalPendingService.findAllList();
    }
    @GetMapping("/admin/document/{id}")
    public ByteArrayResource getDocumentByUserApprovalPendingId(@PathVariable String id){
        return userApprovalPendingService.findDocumentByUserApprovalPendingId(id);
    }
    @PostMapping("/admin/reject-student/{id}")
    public void rejectByUserApprovalPendingId(@PathVariable String id){
        userApprovalPendingService.rejectByUserApprovalPendingId(id);
    }
}

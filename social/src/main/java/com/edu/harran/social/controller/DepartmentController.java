package com.edu.harran.social.controller;

import com.edu.harran.social.dto.DepartmentDto.DepartmentRequestDto;
import com.edu.harran.social.dto.DepartmentDto.DepartmentResponseDto;
import com.edu.harran.social.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api/")
@RequiredArgsConstructor
@CrossOrigin(origins = "*",allowedHeaders = "*")
public class DepartmentController {
    private final DepartmentService departmentService;

    @PostMapping("admin/departments/create")
    public void create(@RequestBody DepartmentRequestDto departmentRequest){
        departmentService.insert(departmentRequest);
    }
    @GetMapping("departments/all")
    public List<DepartmentResponseDto> getAll(){
        return departmentService.findAll();
    }
    @DeleteMapping("admin/departments/delete/{departmentId}")
    public void deleteByDepartmentId(@PathVariable String departmentId){
        departmentService.delete(departmentId);
    }
    @PatchMapping("admin/departments/update/{departmentId}")
    public void updateByDepartmentId(@PathVariable String departmentId,@RequestBody DepartmentRequestDto departmentRequestDto){
        departmentService.update(departmentId,departmentRequestDto);
    }
}

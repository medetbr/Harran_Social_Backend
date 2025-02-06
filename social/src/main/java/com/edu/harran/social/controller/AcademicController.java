package com.edu.harran.social.controller;

import com.edu.harran.social.dto.academicDto.AcademicRequestDto;
import com.edu.harran.social.dto.academicDto.AcademicResponseDto;
import com.edu.harran.social.service.AcademicService;
import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api/admin/academic/")
@RequiredArgsConstructor
@CrossOrigin(origins = "*",allowedHeaders = "*")
public class AcademicController {

    private final AcademicService academicService;

    @PostMapping("create")
    public void create(@RequestBody AcademicRequestDto academicRequest){
       academicService.insert(academicRequest);
       //return ResponseEntity.status(HttpStatus.OK).body(academicRequest.getName()+" Başarılı bir şekilde eklendi");
    }
    @GetMapping("all")
    public List<AcademicResponseDto> getAll(){
        return academicService.findAll();
    }
    @DeleteMapping("delete/{academicId}")
    public void deleteByAcademicId(@PathVariable String academicId){
        academicService.delete(academicId);
    }
    @PatchMapping("update/{academicId}")
    public void updateByAcademicId(@PathVariable String academicId,@RequestBody AcademicRequestDto academicRequestDto){
        academicService.update(academicId,academicRequestDto);
    }
}
